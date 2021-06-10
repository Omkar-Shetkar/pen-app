package com.light.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.request.client.RequestUpdate;
import com.light.request.domain.Request;
import com.light.request.repository.ExamScheduleRepository;
import com.light.scribe.service.ScribeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.light.request.RequestController.ALL;
import static com.light.request.RequestController.SINGLE;
import static com.light.request.domain.RequestStatus.ACCEPTED;
import static com.light.request.domain.RequestStatus.REJECTED;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RequestControllerTests extends BaseTestSetup {

    public static final long CANDIDATE = 1L;
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
    private ExamScheduleRepository examScheduleRepository;

    @Test
    void candidateRequestsForExam() throws Exception {

        RequestUpdate requestUpdate = createRequestUpdate(1L);
        String content = mapper.writeValueAsString(requestUpdate);
        mockMvc.perform(post(ALL).contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    void scribeAcceptsRequest() throws Exception {

        Request request = createRequest(1L);

        // Scribe accepts the request
        String content = mapper.writeValueAsString(ACCEPTED);
        String response = mockMvc.perform(patch(SINGLE, request.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        request = mapper.readValue(response, Request.class);
        assertEquals(ACCEPTED, request.getStatus());

        // Scribe has rejected the request
        scribeRejectsRequest(request);

        // Scribe tries to accept the request again
        content = mapper.writeValueAsString(ACCEPTED);
        mockMvc.perform(patch(SINGLE, request.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());

    }

    private RequestUpdate createRequestUpdate(long scribeId) {
        // Candidate requests for exam
        Instant start = LocalDateTime.now().plusDays(2)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = start.plus(2, HOURS);

        return new RequestUpdate(scribeId, CANDIDATE, start, end, LANGUAGE_ID, ADDRESS_ID, EXAM_ID);

    }

    private Request createRequest(long scribeId) {
        try {
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

    private void scribeRejectsRequest(Request request) throws Exception {
        String content = mapper.writeValueAsString(REJECTED);
        mockMvc.perform(patch(SINGLE, request.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

}