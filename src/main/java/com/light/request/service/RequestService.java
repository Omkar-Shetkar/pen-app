package com.light.request.service;

import com.light.candidate.domain.Candidate;
import com.light.candidate.service.CandidateService;
import com.light.exam.client.ExamEventUpdate;
import com.light.exam.domain.ExamSchedule;
import com.light.exam.service.ExamEventService;
import com.light.request.client.RequestResource;
import com.light.request.client.RequestSearchFilter;
import com.light.request.client.RequestUpdate;
import com.light.request.domain.Request;
import com.light.request.domain.RequestStatus;
import com.light.request.repository.ExamScheduleRepository;
import com.light.request.repository.RequestRepository;
import com.light.scribe.domain.Scribe;
import com.light.scribe.service.ScribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static com.light.request.domain.RequestStatus.*;

@Service
@Transactional
@Slf4j
public class RequestService {
    public static final int REQUEST_CANCELLATION_CUT_OFF_HRS = 12;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private ScribeService scribeService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ExamEventService examEventService;

    public Request create(RequestUpdate requestUpdate) {

        Scribe scribe = scribeService.findById(requestUpdate.getScribeId());
        Candidate candidate = candidateService.findById(requestUpdate.getCandidateId());

        // Use existing exam schedule, if available
        ExamSchedule examSchedule = examScheduleRepository.find(requestUpdate.getExam(),
                requestUpdate.getStartTime(), requestUpdate.getEndTime(), requestUpdate.getLanguage())
                .orElseGet(() ->
                        {
                            ExamSchedule newSchedule = new ExamSchedule(requestUpdate.getStartTime(),
                                    requestUpdate.getEndTime(), requestUpdate.getLanguage(),
                                    requestUpdate.getAddress(), requestUpdate.getExam());
                            return examScheduleRepository.save(newSchedule);
                        }
                );

        Request request = new Request(scribe.getId(), candidate.getId(), examSchedule.getId());

        return requestRepository.save(request);

    }

    public Request update(long id, RequestStatus targetStatus) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find request id: " + id));

        if (request.getStatus() == targetStatus) {
            throw new IllegalStateException(String.format("Request {%d} is already in {%s} status!", request.getId(), targetStatus));
        }

        return updateStatus(request, targetStatus);
    }

    public void deleteRequest(long requestId) {
        requestRepository.deleteById(requestId);
    }

    public void deleteByExamScheduleAndNotStatus(long examSchedule, RequestStatus status) {
        requestRepository.deleteByExamScheduleAndNotStatus(examSchedule, status);
    }

    /**
     * Updates status for all related requests for the given Candidate and exam schedule
     *
     * @param request      {@link Request} to be updated with target status
     * @param targetStatus Intended status for the request
     * @return Updated {@link Request} instance
     */
    private Request updateStatus(Request request, RequestStatus targetStatus) {

        RequestStatus remainingRequestStatus = PENDING;

        switch (request.getStatus()) {
            case PENDING:
                if (targetStatus != PENDING) {
                    if (targetStatus == ACCEPTED) {
                        transitionToAcceptedStatus(request);
                        remainingRequestStatus = DISABLED;
                    }
                    request.setStatus(targetStatus);
                }
                break;
            case ACCEPTED:
                if (targetStatus == REJECTED) {
                    transitionToRejectedStatus(request);
                }
                break;
            case REJECTED:
                // No status transition allowed for REJECTED request
                throw new IllegalArgumentException(String.format("Invalid status transition request from [%s] to [%s]",
                        request.getStatus(), targetStatus));
            case DISABLED:
                if (targetStatus == PENDING) {
                    request.setStatus(targetStatus);
                }
                break;
            default:
                throw new IllegalStateException("Invalid state present in stored request : " + request);
        }

        List<Request> allRequests = updateRemainingRequests(request, remainingRequestStatus);

        allRequests.add(request);

        requestRepository.saveAll(allRequests);

        return request;
    }

    private void transitionToAcceptedStatus(Request request) {
        if (isSlotAssignableToScribe(request)) {
            request.setStatus(ACCEPTED);
            // Scribe accepted the request. Hence, create exam event.
            ExamEventUpdate examEventUpdate = new ExamEventUpdate(request);
            examEventService.create(examEventUpdate);
        } else {
            throw new IllegalStateException(String.format("Scribe {%d} is already writing for the given time slot {%s}!",
                    request.getScribe(), request.getExamSchedule().toString()));
        }
    }

    private void transitionToRejectedStatus(Request request) {
        if (isRequestRejectable(request)) {
            request.setStatus(REJECTED);
            examEventService.delete(request.getId());
        } else {
            throw new IllegalStateException(String.format("Can't transition to REJECTED status for the request {%d}", request.getId()));
        }
    }

    /**
     * A request is rejectable, if its before specified cut-off time (Eg., 12 Hrs before start of exam)
     *
     * @param request - the {@link Request}
     * @return - true if Request can be rejected else false
     */
    private boolean isRequestRejectable(Request request) {
        if (request.getStatus() == ACCEPTED) {
            var examSchedule = examScheduleRepository.findById(request.getExamSchedule())
                    .orElseThrow(() -> new IllegalStateException("No exam schedule exists for request: " + request.getId()));
            long durationInHrs = Duration.between(Instant.now(), examSchedule.getStartTime().toInstant()).toHours();
            return durationInHrs > REQUEST_CANCELLATION_CUT_OFF_HRS;
        }
        return false;
    }

    private boolean isSlotAssignableToScribe(Request request) {
        RequestSearchFilter searchFilter = RequestSearchFilter.builder()
                .scribe(request.getScribe())
                .schedule(request.getExamSchedule())
                .requestStatus(ACCEPTED).build();
        List<Request> requests = search(searchFilter);
        return requests.isEmpty();
    }

    private List<Request> updateRemainingRequests(Request request, RequestStatus remainingRequestStatus) {
        // Remaining requests of Candidate should be updated for corresponding status
        RequestSearchFilter searchFilter = new RequestSearchFilter();
        searchFilter.setCandidate(request.getCandidate());
        searchFilter.setSchedule(request.getExamSchedule());
        List<Request> allRequests = search(searchFilter);

        allRequests.stream()
                .filter(r -> !r.getId().equals(request.getId()))
                .forEach(r -> r.setStatus(remainingRequestStatus));
        return allRequests;
    }

    public List<Request> search(RequestSearchFilter searchFilter) {
        return requestRepository.searchRequests(searchFilter.getCandidate(), searchFilter.getScribe(),
                searchFilter.getSchedule(), searchFilter.getRequestStatus());
    }

    public List<RequestResource> searchRequestResources(RequestSearchFilter searchFilter) {
        List<RequestResource> requests = requestRepository.searchRequestResources(searchFilter.getCandidate(),
                searchFilter.getScribe(), searchFilter.getSchedule(),
                searchFilter.getRequestStatus(), searchFilter.getStartingFrom());
        return requests;
    }
}