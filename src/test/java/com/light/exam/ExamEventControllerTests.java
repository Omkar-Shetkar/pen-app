package com.light.exam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.exam.controller.ExamEventController;
import com.light.exam.domain.ExamEvent;
import com.light.exam.service.ExamEventService;
import com.light.request.client.RequestUpdate;
import com.light.request.domain.Request;
import com.light.request.repository.ExamScheduleRepository;
import com.light.scribe.domain.Scribe;
import com.light.scribe.service.ScribeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.light.request.RequestController.ALL;
import static com.light.request.RequestController.SINGLE;
import static com.light.request.domain.RequestStatus.ACCEPTED;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamEventControllerTests extends BaseTestSetup {

    public static final long CANDIDATE_ID = 1L;
    public static final long EXAM_ID = 101;
    public static final long LANGUAGE_ID = 1;
    public static final long ADDRESS_ID = 1;

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
                .param("candidateId", String.valueOf(CANDIDATE_ID)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ExamEvent> examEvents = mapper.readValue(response, List.class);
        assertEquals(1, examEvents.size());
    }

    private Request candidateCreatesRequestAndScribeAcceptsIt() throws Exception {
        Instant start = LocalDateTime.now().plusDays(2)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = start.plus(2, HOURS);

        // Candidate sends request to all scribes
        Iterable<Scribe> scribes = scribeService.getAll();

        List<Request> requests = new ArrayList<>();
        StreamSupport.stream(scribes.spliterator(), false)
                .forEach(s -> {
                    Request request = createRequest(s.getId());
                    requests.add(request);
                });

        // Scribe accepts the request
        String content = mapper.writeValueAsString(ACCEPTED);
        String response = mockMvc.perform(patch(SINGLE, requests.get(0).getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Request request = mapper.readValue(response, Request.class);

        assertEquals(ACCEPTED, request.getStatus());
        return request;
    }

    private RequestUpdate createRequestUpdate(long scribeId) {
        // Candidate requests for exam
        Instant start = LocalDateTime.now().plusDays(2)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = start.plus(2, HOURS);

        return new RequestUpdate(scribeId, CANDIDATE_ID, start, end, LANGUAGE_ID, ADDRESS_ID, EXAM_ID);

    }

    private Request createRequest(long scribeId) {
        try {
            // Candidate requests for exam
            RequestUpdate requestUpdate = createRequestUpdate(scribeId);
            String content = mapper.writeValueAsString(requestUpdate);
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