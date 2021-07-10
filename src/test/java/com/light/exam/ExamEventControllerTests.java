package com.light.exam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.exam.client.FeedbackDetails;
import com.light.exam.client.UserType;
import com.light.exam.controller.ExamEventController;
import com.light.exam.domain.ExamEvent;
import com.light.exam.domain.ExamSchedule;
import com.light.exam.service.ExamEventService;
import com.light.request.client.RequestCreationModel;
import com.light.request.client.RequestUpdateModel;
import com.light.request.domain.Request;
import com.light.request.repository.ExamScheduleRepository;
import com.light.scribe.domain.Scribe;
import com.light.scribe.service.ScribeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.light.exam.controller.ExamEventController.FEEDBACK;
import static com.light.request.RequestController.ALL;
import static com.light.request.RequestController.SINGLE;
import static com.light.request.domain.RequestStatus.ACCEPTED;
import static com.light.request.domain.RequestStatus.REJECTED;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamEventControllerTests extends BaseTestSetup {

    public static final long CANDIDATE_ID = 1L;
    public static final long EXAM_ID = 101L;
    public static final long LANGUAGE_ID = 1L;
    public static final long ADDRESS_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ScribeService scribeService;
    @Autowired
    private ExamEventService examEventService;
    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Test
    void testCreateExamEvent() throws Exception {
        candidateCreatesRequestAndScribeAcceptsIt();

        // Assert creation of exam event for Candidate and Scribe
        String response = mockMvc.perform(get(ExamEventController.ALL)
                .param("candidate", String.valueOf(CANDIDATE_ID)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ExamEvent> examEvents = mapper.readValue(response, List.class);
        assertEquals(1, examEvents.size());
    }

    /**
     * With Scribe rejecting request, corresponding exam event deleted.
     */
    @Test
    void testDeleteExamEvent() throws Exception {
        Request request = candidateCreatesRequestAndScribeAcceptsIt();
        rejectRequest(request).andExpect(status().isOk());

        // Assert no exam event for the candidate
        String response = mockMvc.perform(get(ExamEventController.ALL)
                .param("candidate", String.valueOf(CANDIDATE_ID)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ExamEvent> examEvents = mapper.readValue(response, List.class);
        Assertions.assertEquals(0, examEvents.size());
    }


    /**
     * Candidate can't cancel the request 12Hrs before start of exam.
     */
    @Test
    void candidateTriesCancelsRequestAndExamEventNotDeleted() throws Exception {
        ExamSchedule examSchedule = createExamSchedule(LocalDateTime.now().plusHours(10));
        Request request = candidateCreatesRequestAndScribeAcceptsIt(examSchedule);
        rejectRequest(request).andExpect(status().isBadRequest());

        // Assert exam event exists for the candidate for the given time schedule
        String response = mockMvc.perform(get(ExamEventController.ALL)
                .param("candidate", String.valueOf(CANDIDATE_ID)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ExamEvent> examEvents = mapper.readValue(response, List.class);
        Assertions.assertEquals(1, examEvents.size());

    }

    /**
     * Candidate and Scribe give feedback on each other after completion of exam.
     */
    @Test
    void updateExamEventWithFeedback() throws Exception {
        ExamSchedule examSchedule = createExamSchedule(LocalDateTime.now().plusHours(10));
        Request request = candidateCreatesRequestAndScribeAcceptsIt(examSchedule);

        ExamEvent examEvent = examEventService.getByRequestId(request.getId())
                .orElseThrow(()
                        -> new IllegalStateException(String.format("Exam event for request id {%d} not found!",
                        request.getId())));

        // Scribe gives feedback
        FeedbackDetails feedbackDetails = new FeedbackDetails();
        feedbackDetails.setRating(5);
        feedbackDetails.setComment("Well tried!!");
        feedbackDetails.setUserType(UserType.SCRIBE);
        feedbackDetails.setUserId(examEvent.getScribe());
        String content = mapper.writeValueAsString(feedbackDetails);

        mockMvc.perform(patch(FEEDBACK, examEvent.getId())
                .with(csrf()).contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNoContent());

        ExamEvent event = examEventService.getById(examEvent.getId())
                .orElseThrow(()
                        -> new IllegalStateException(String.format("Exam event with id {%d} not found!",
                        examEvent.getId())));
        assertEquals(5, event.getScribeFeedback().getRating());
        assertEquals("Well tried!!", event.getScribeFeedback().getComment());


        // Candidate gives feedback
        feedbackDetails = new FeedbackDetails();
        feedbackDetails.setRating(5);
        feedbackDetails.setComment("Thanks!!");
        feedbackDetails.setUserType(UserType.CANDIDATE);
        feedbackDetails.setUserId(examEvent.getCandidate());
        content = mapper.writeValueAsString(feedbackDetails);

        mockMvc.perform(patch(FEEDBACK, examEvent.getId())
                .with(csrf()).contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNoContent());

        event = examEventService.getById(examEvent.getId())
                .orElseThrow(()
                        -> new IllegalStateException(String.format("Exam event with id {%d} not found!",
                        examEvent.getId())));
        assertEquals(5, event.getCandidateFeedback().getRating());
        assertEquals("Thanks!!", event.getCandidateFeedback().getComment());

    }

    private ResultActions rejectRequest(Request request) throws Exception {
        String content = mapper.writeValueAsString(new RequestUpdateModel(REJECTED));
        return mockMvc.perform(patch(SINGLE, request.getId())
                .with(csrf()).contentType(APPLICATION_JSON)
                .content(content));
    }

    private Request candidateCreatesRequestAndScribeAcceptsIt(ExamSchedule examSchedule) throws Exception {

        // Candidate sends request to all scribes
        Iterable<Scribe> scribes = scribeService.getAll();

        List<Request> requests = new ArrayList<>();
        StreamSupport.stream(scribes.spliterator(), false)
                .forEach(s -> {
                    Request request = createRequest(s.getId(), examSchedule);
                    requests.add(request);
                });

        // Scribe accepts the request
        String content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
        String response = mockMvc.perform(patch(SINGLE, requests.get(0).getId())
                .with(csrf()).contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Request request = mapper.readValue(response, Request.class);

        assertEquals(ACCEPTED, request.getStatus());
        return request;
    }

    private Request candidateCreatesRequestAndScribeAcceptsIt() throws Exception {
        ExamSchedule examSchedule = createExamSchedule();
        return candidateCreatesRequestAndScribeAcceptsIt(examSchedule);
    }

    private ExamSchedule createExamSchedule(LocalDateTime start) {
        Instant startTime = start.atZone(ZoneId.systemDefault()).toInstant();
        Instant endTime = startTime.plus(2, HOURS);

        ExamSchedule examSchedule = new ExamSchedule();
        examSchedule.setStartTime(new Date(startTime.toEpochMilli()));
        examSchedule.setEndTime(new Date(endTime.toEpochMilli()));
        examSchedule.setLanguage(LANGUAGE_ID);
        examSchedule.setAddress(ADDRESS_ID);
        examSchedule.setExam(EXAM_ID);
        return examSchedule;
    }

    private ExamSchedule createExamSchedule() {
        Instant start = LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant();
        Instant end = start.plus(2, HOURS);

        ExamSchedule examSchedule = new ExamSchedule();
        examSchedule.setStartTime(new Date(start.toEpochMilli()));
        examSchedule.setEndTime(new Date(end.toEpochMilli()));
        examSchedule.setLanguage(LANGUAGE_ID);
        examSchedule.setAddress(ADDRESS_ID);
        examSchedule.setExam(EXAM_ID);
        return examSchedule;
    }


    private RequestCreationModel createRequestUpdate(long scribeId, ExamSchedule examSchedule) {
        return new RequestCreationModel(scribeId, CANDIDATE_ID, examSchedule.getStartTime(), examSchedule.getEndTime(),
                examSchedule.getLanguage(), examSchedule.getAddress(), examSchedule.getExam());
    }

    private Request createRequest(long scribeId, ExamSchedule examSchedule) {
        try {
            // Candidate requests for exam
            RequestCreationModel requestCreationModel = createRequestUpdate(scribeId, examSchedule);
            String content = mapper.writeValueAsString(requestCreationModel);
            String response = mockMvc.perform(post(ALL).with(csrf()).contentType(APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();
            return mapper.readValue(response, Request.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}