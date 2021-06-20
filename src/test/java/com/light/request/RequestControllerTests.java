package com.light.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.exam.domain.ExamSchedule;
import com.light.request.client.RequestUpdate;
import com.light.request.domain.Request;
import com.light.request.domain.RequestStatus;
import com.light.request.repository.ExamScheduleRepository;
import com.light.scribe.domain.Scribe;
import com.light.scribe.service.ScribeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

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
import static com.light.request.domain.RequestStatus.REJECTED;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void testCandidateRequestsForExam() throws Exception {

        RequestUpdate requestUpdate = createRequestUpdate(1L);
        String content = mapper.writeValueAsString(requestUpdate);
        mockMvc.perform(post(ALL).contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    void testScribeAcceptsRequest() throws Exception {

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

    /**
     * Candidate raises request with multiple scribes for an exam for a specific exam schedule.
     * A scribe accepts the request. Then all remaining requests raised for corresponding exam schedule become disabled.
     * But after some time same scribe rejects the accepted request.
     * Then, remaining requests raised for corresponding exam schedule get enabled again.
     */
    @Test
    @DisplayName("Other scribe accepts and rejects the request")
    void testRequestDisableStatus() throws Exception {

        Request request = candidateCreatesRequestAndScribeAcceptsIt();

        //************** Remaining requests for the same schedule should be disabled **********************

        // Get all requests for the given Candidate and exam schedule
        Long examScheduleId = request.getExamSchedule();
        ExamSchedule examSchedule = examScheduleRepository.findById(examScheduleId)
                .orElseThrow(() -> new IllegalStateException("Exam schedule not found " + examScheduleId));
        String response = mockMvc.perform(get((ALL))
                .param("candidate", "1")
                .param("schedule", String.valueOf(examSchedule.getId()))
                .param("requestStatus", RequestStatus.DISABLED.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<LinkedHashMap<String, String>> requestSet = mapper.readValue(response, List.class);
        Assertions.assertEquals(2, requestSet.size());
        Assertions.assertEquals(requestSet.iterator().next().get("status"), RequestStatus.DISABLED.toString());

        //************ Scribe rejects the request, then remaining requests get enabled again *********************
        scribeRejectsRequest(request);

        // Get all requests for the given Candidate and exam schedule
        response = mockMvc.perform(get((ALL))
                .param("candidate", String.valueOf(request.getCandidate()))
                .param("schedule", String.valueOf(examSchedule.getId()))
                .param("requestStatus", RequestStatus.PENDING.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        requestSet = mapper.readValue(response, List.class);
        Assertions.assertEquals(2, requestSet.size());
        Assertions.assertEquals(requestSet.iterator().next().get("status"), RequestStatus.PENDING.toString());
    }

    /**
     * Suppose Candidate raised request for an exam schedule.
     * No Scribe accepted the request yet.
     * Now, Candidate again searches for Scribe for the schedule. He finds new Scribes.
     * Then, he should be able to raise request for the same schedule.
     */
    @Test
    void testRequestSomeMoreScribes() throws Exception {
        // Candidate requests a Scribe
        Request request = createRequest(1L);

        // Since no response from Scribe, Candidate requests some more Scribes for the same schedule
        request = createRequest(2l);

        Long examSchedule = request.getExamSchedule();

        // Now, Candidate should have total of two requests for the corresponding schedule
        String response = mockMvc.perform(get((ALL))
                .param("candidate", String.valueOf(request.getCandidate()))
                .param("schedule", String.valueOf(examSchedule))
                .param("requestStatus", RequestStatus.PENDING.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<LinkedHashMap<String, String>> requestSet = mapper.readValue(response, List.class);
        Assertions.assertEquals(2, requestSet.size());

    }

    /**
     * Scribe can't accept more than one request for the same time slot.
     */
    @Test
    void testMultipleRequestsForSameTimeSlot() throws Exception {
        Request request = candidateCreatesRequestAndScribeAcceptsIt();

        // Scribe accepts the request
        String content = mapper.writeValueAsString(ACCEPTED);
        mockMvc.perform(patch(SINGLE, request.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());

        // Now, scribe should have only one request in ACCEPTED status
        String response = mockMvc.perform(get((ALL))
                .param("scribe", String.valueOf(request.getScribe()))
                .param("requestStatus", ACCEPTED.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<LinkedHashMap<String, String>> requestSet = mapper.readValue(response, List.class);
        Assertions.assertEquals(1, requestSet.size());
        Assertions.assertEquals(String.valueOf(request.getId()), String.valueOf(requestSet.iterator().next().get("id")));
    }

    private Request candidateCreatesRequestAndScribeAcceptsIt() throws Exception {
        // Candidate sends request to all scribes
        Iterable<Scribe> scribes = scribeService.getAll();

        List<Request> requests = new ArrayList<>();
        for (Scribe scribe : scribes) {
            Request request = createRequest(scribe.getId());
            requests.add(request);
        }

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

        return new RequestUpdate(scribeId, CANDIDATE, new Date(start.toEpochMilli()), new Date(end.toEpochMilli()),
                LANGUAGE_ID, ADDRESS_ID, EXAM_ID);

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