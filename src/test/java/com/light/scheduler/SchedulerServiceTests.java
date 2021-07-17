package com.light.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.exam.domain.ExamEvent;
import com.light.exam.domain.ExamSchedule;
import com.light.exam.service.ExamEventService;
import com.light.client.request.RequestCreationModel;
import com.light.client.request.RequestUpdateModel;
import com.light.request.domain.Request;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.light.request.RequestController.ALL;
import static com.light.request.RequestController.SINGLE;
import static com.light.request.domain.RequestStatus.ACCEPTED;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SchedulerServiceTests extends BaseTestSetup {

    public static final String CANDIDATE_ID = "100";
    public static final String SCRIBE_ID = "1";
    public static final long EXAM_ID = 101;
    public static final long LANGUAGE_ID = 1L;
    public static final long ADDRESS_ID = 1L;

    @SpyBean
    private SchedulerService schedulerService;

    @Autowired
    private ExamEventService examEventService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testSchedulingOfOtpGeneration() {
        // Candidate creates a request
        ExamSchedule examSchedule = createExamSchedule(LocalDateTime.now().plusMinutes(30));
        Request request = candidateCreatesRequestAndScribeAcceptsIt(SCRIBE_ID, examSchedule);
        assertEquals(ACCEPTED, request.getStatus());

        long requestId = request.getId();
        // On the day of exam, OTP is generated for the exam event
        Awaitility.waitAtMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            Mockito.verify(schedulerService, Mockito.atLeast(2)).scheduleOtpGeneration();
            ExamEvent examEvent = examEventService.getByRequestId(requestId)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Exam event with request id {%d} not found!",
                            requestId)));
            assertTrue(examEvent.getOtpGenerated());
        });

    }

    //FIXME: Unit test fix
    @Test
    void testNonSchedulingOfOtpGeneration() {
        // Candidate creates a request
        ExamSchedule examSchedule = createExamSchedule(LocalDateTime.now().plusHours(3));
        Request request = candidateCreatesRequestAndScribeAcceptsIt(SCRIBE_ID, examSchedule);
        assertEquals(ACCEPTED, request.getStatus());

        long requestId = request.getId();
        // On the day of exam, OTP is generated for the exam event
        Awaitility.waitAtMost(Duration.of(15, SECONDS)).untilAsserted(() -> {
            Mockito.verify(schedulerService, Mockito.atLeast(1)).scheduleOtpGeneration();
            ExamEvent examEvent = examEventService.getByRequestId(requestId)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Exam event with request id {%d} not found!",
                            requestId)));
            assertNull(examEvent.getOtpGenerated());
        });
    }

    private ExamSchedule createExamSchedule(LocalDateTime start) {
        Instant startTime = start.atZone(ZoneId.systemDefault()).toInstant();
        Instant endTime = startTime.plus(2, HOURS)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        ExamSchedule examSchedule = new ExamSchedule();
        examSchedule.setStartTime(new Date(startTime.toEpochMilli()));
        examSchedule.setEndTime(new Date(endTime.toEpochMilli()));
        examSchedule.setLanguage(LANGUAGE_ID);
        examSchedule.setAddress(ADDRESS_ID);
        examSchedule.setExam(EXAM_ID);

        return examSchedule;
    }

    private RequestCreationModel createRequestUpdate(String scribeId, ExamSchedule examSchedule) {
        return new RequestCreationModel(scribeId, CANDIDATE_ID, examSchedule.getStartTime(), examSchedule.getEndTime(),
                examSchedule.getLanguage(), examSchedule.getAddress(), examSchedule.getExam());
    }

    private Request candidateCreatesRequestAndScribeAcceptsIt(String scribeId, ExamSchedule examSchedule) {
        try {
            // Candidate requests for exam
            RequestCreationModel requestCreationModel = createRequestUpdate(scribeId, examSchedule);
            String content = mapper.writeValueAsString(requestCreationModel);
            String response = mockMvc.perform(post(ALL).contentType(APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();
            Request request = mapper.readValue(response, Request.class);

            // Scribe accepts the request
            content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
            long requestId = request.getId();
            response = mockMvc.perform(patch(SINGLE, requestId)
                    .contentType(APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            return mapper.readValue(response, Request.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
