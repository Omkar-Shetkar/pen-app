package com.light.request.service;

import com.light.candidate.domain.Candidate;
import com.light.candidate.service.CandidateService;
import com.light.exam.domain.ExamSchedule;
import com.light.exam.service.ExamEventService;
import com.light.request.client.RequestUpdate;
import com.light.request.domain.Request;
import com.light.request.repository.ExamScheduleRepository;
import com.light.request.repository.RequestRepository;
import com.light.scribe.domain.Scribe;
import com.light.scribe.service.ScribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //TODO: Use existing exam schedule, if available
//        Optional<ExamSchedule> scheduleOptional = examScheduleRepository
//                .findByStartTimeAndEndTime(schedule.getStartTime(), schedule.getEndTime());

        ExamSchedule examSchedule = new ExamSchedule(requestUpdate.getStartTime(),
                requestUpdate.getEndTime(), requestUpdate.getLanguage(),
                requestUpdate.getAddress(), requestUpdate.getExam());
        ExamSchedule savedSchedule = examScheduleRepository.save(examSchedule);

        Request request = new Request(scribe.getId(), candidate.getId(), savedSchedule.getId());

        return requestRepository.save(request);

    }
}