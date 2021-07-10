package com.light;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.exam.controller.ExamEventController;
import com.light.exam.domain.ExamEvent;
import com.light.exam.domain.ExamSchedule;
import com.light.exam.domain.ExamStatus;
import com.light.exam.service.ExamEventService;
import com.light.otp.client.OtpRequest;
import com.light.otp.service.OtpService;
import com.light.request.client.RequestCreationModel;
import com.light.request.client.RequestSearchFilter;
import com.light.request.client.RequestUpdateModel;
import com.light.request.domain.Request;
import com.light.request.service.RequestService;
import com.light.scheduler.SchedulerService;
import com.light.scribe.domain.Scribe;
import com.light.scribe.service.ScribeService;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.light.request.RequestController.ALL;
import static com.light.request.RequestController.SINGLE;
import static com.light.request.domain.RequestStatus.ACCEPTED;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LightBackendApplicationTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ScribeService scribeService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ExamEventService examEventService;

    @Autowired
    private OtpService otpService;

    @SpyBean
    private SchedulerService schedulerService;

    public static final long CANDIDATE_ID = 1L;
    public static final long SCRIBE_ID = 1L;
    public static final long EXAM_ID = 101L;
    public static final long LANGUAGE_ID = 1L;
    public static final long ADDRESS_ID = 1L;

    @Test
    void applicationLifeCycle() throws Exception {
        ExamSchedule examSchedule = createExamSchedule(LocalDateTime.now().plusSeconds(25));
        Request request = candidateCreatesRequestAndScribeAcceptsIt(examSchedule);
        long requestId = request.getId();

        ExamEvent examEvent = examEventService.getByRequestId(requestId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Exam event with request id {%d} not found!",
                        requestId)));
        long examEventId = examEvent.getId();
        assertEquals(ExamStatus.NOT_STARTED, examEvent.getExamStatus());

        // On the day of exam, OTP is generated for the exam event
        Awaitility.waitAtMost(Durations.ONE_MINUTE).untilAsserted(() -> {
            Mockito.verify(schedulerService, Mockito.atLeast(1)).scheduleOtpGeneration();
            ExamEvent updatedExamEvent = examEventService.getById(examEventId)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Exam event with id {%d} not found!",
                            examEventId)));
            assertTrue(updatedExamEvent.getOtpGenerated());

            // Create OTP request
            OtpRequest otpRequest = new OtpRequest(updatedExamEvent.getCandidate(), updatedExamEvent.getScribe());
            otpRequest.setOtp(otpService.get(otpRequest));
            // Candidate verifies OTP with Scribe
            mockMvc.perform(get(ExamEventController.VERIFY, updatedExamEvent.getId())
                    .param("candidateId", String.valueOf(CANDIDATE_ID))
                    .param("scribeId", String.valueOf(SCRIBE_ID))
                    .param("otp", String.valueOf(otpRequest.getOtp())))
                    .andExpect(status().isOk());
            assertEquals(ExamStatus.STARTED, updatedExamEvent.getExamStatus());
        });

        /**
         * Exam completes and expect
         * - Mark completed exam event as COMPLETED
         * - All but ACCEPTED request to be deleted
         */
        Duration duration = Duration.between(Instant.now(), examSchedule.getEndTime().toInstant());
        Awaitility.waitAtMost(duration).untilAsserted(() -> {
            ExamEvent updatedExamEvent = examEventService.getById(examEventId)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Exam event with id {%d} not found!",
                            examEventId)));
            Mockito.verify(schedulerService, Mockito.atLeast(1)).scheduleExamCompletion();
            assertEquals(ExamStatus.COMPLETED, updatedExamEvent.getExamStatus());

            RequestSearchFilter filter = new RequestSearchFilter();
            filter.setSchedule(examEvent.getExamSchedule());
            List<Request> search = requestService.search(filter);
            assertEquals(1, search.size());
            assertEquals(ACCEPTED, search.get(0).getStatus());
        });

        /**
         * Get archived exam events for the candidate
         */
        String response = mockMvc.perform(get((ExamEventController.ALL))
                .param("candidate", String.valueOf(CANDIDATE_ID))
                .param("examStatus", ExamStatus.COMPLETED.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<LinkedHashMap<String, String>> archiveExamEvents = mapper.readValue(response, List.class);
        Assertions.assertEquals(1, archiveExamEvents.size());
        assertEquals(ExamStatus.COMPLETED.toString(), archiveExamEvents.get(0).get("examStatus"));

    }

    private Request candidateCreatesRequestAndScribeAcceptsIt(ExamSchedule schedule) throws Exception {
        // Candidate sends request to all scribes
        Iterable<Scribe> scribes = scribeService.getAll();

        List<Request> requests = new ArrayList<>();
        for (Scribe scribe : scribes) {
            Request request = createRequest(scribe.getId(), schedule);
            requests.add(request);
        }

        // Scribe accepts the request
        String content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
        String response = mockMvc.perform(patch(SINGLE, requests.get(0).getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Request request = mapper.readValue(response, Request.class);
        assertEquals(ACCEPTED, request.getStatus());
        return request;
    }

    private ExamSchedule createExamSchedule(LocalDateTime start) {
        Instant startTime = start.atZone(ZoneId.systemDefault()).toInstant();
        Instant endTime = startTime.plus(10, SECONDS);

        ExamSchedule examSchedule = new ExamSchedule();
        examSchedule.setStartTime(new Date(startTime.toEpochMilli()));
        examSchedule.setEndTime(new Date(endTime.toEpochMilli()));
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
            String response = mockMvc.perform(post(ALL).contentType(APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();
            return mapper.readValue(response, Request.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
