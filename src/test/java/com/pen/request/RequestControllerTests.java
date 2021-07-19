package com.pen.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pen.BaseTestSetup;
import com.pen.exam.domain.ExamSchedule;
import com.pen.client.request.RequestCreationModel;
import com.pen.client.request.RequestResource;
import com.pen.client.request.RequestUpdateModel;
import com.pen.request.domain.Request;
import com.pen.request.domain.RequestStatus;
import com.pen.request.repository.ExamScheduleRepository;
import com.pen.scribe.domain.Scribe;
import com.pen.scribe.service.ScribeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.pen.request.RequestController.ALL;
import static com.pen.request.RequestController.SINGLE;
import static com.pen.request.domain.RequestStatus.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RequestControllerTests extends BaseTestSetup {

    public static final String CANDIDATE = "100";
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

        RequestCreationModel requestCreationModel = createRequestUpdate("1");
        String content = mapper.writeValueAsString(requestCreationModel);
        mockMvc.perform(post(ALL).contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());

        // Get all 'PENDING' requests of candidate
        String response = mockMvc.perform(get(ALL).contentType(APPLICATION_JSON)
                .param("requestStatus", PENDING.toString())
                .param("candidate", String.valueOf(CANDIDATE)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<LinkedHashMap<String, Object>> pendingRequests = mapper.readValue(response, List.class);
        assertEquals(1, pendingRequests.size());
        LinkedHashMap<String, String> candidateResource =
                (LinkedHashMap<String, String>) pendingRequests.get(0).get("candidateResource");
        assertEquals("candidate1-fn", candidateResource.get("firstName"));

    }

    @Test
    public void candidateCancelsRequest() throws Exception {
        RequestCreationModel requestCreationModel = createRequestUpdate("1");
        String content = mapper.writeValueAsString(requestCreationModel);
        String response = mockMvc.perform(post(ALL).contentType(APPLICATION_JSON)
                
                .content(content))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Request request = mapper.readValue(response, Request.class);

        // Candidate cancels the request
        mockMvc.perform(delete(SINGLE, request.getId()))
                .andExpect(status().isNoContent());

        // Get all 'PENDING' requests of candidate
        response = mockMvc.perform(get(ALL).contentType(APPLICATION_JSON)
                .param("requestStatus", PENDING.toString())
                .param("candidate", String.valueOf(CANDIDATE)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<LinkedHashMap<String, Object>> pendingRequests = mapper.readValue(response, List.class);
        assertEquals(0, pendingRequests.size());

    }

    @Test
    void testListRequestsFromGivenStartDate() throws Exception {
        RequestCreationModel requestCreationModel = createRequestUpdate("1");
        String content = mapper.writeValueAsString(requestCreationModel);
        mockMvc.perform(post(ALL).contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());

        // Get all 'PENDING' requests of candidate
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String response = mockMvc.perform(get(ALL).contentType(APPLICATION_JSON)
                .param("requestStatus", PENDING.toString())
                .param("candidate", String.valueOf(CANDIDATE))
                .param("startingFrom", format.format(new Date(Instant.now().toEpochMilli()))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<RequestResource> pendingRequests = mapper.readValue(response, List.class);
        assertEquals(1, pendingRequests.size());


        response = mockMvc.perform(get(ALL).contentType(APPLICATION_JSON)
                .param("requestStatus", PENDING.toString())
                .param("candidate", String.valueOf(CANDIDATE))
                .param("startingFrom", format.format(new Date(Instant.now().plus(3, DAYS).toEpochMilli()))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        pendingRequests = mapper.readValue(response, List.class);
        assertEquals(0, pendingRequests.size());

    }

    @Test
    void testScribeAcceptsRequest() throws Exception {

        Request request = createRequest("1");

        // Scribe accepts the request
        String content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
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
        content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
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
                .param("candidate", "100")
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
        Request request = createRequest("1");

        // Since no response from Scribe, Candidate requests some more Scribes for the same schedule
        request = createRequest("2");

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
        String content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
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

    private RequestCreationModel createRequestUpdate(String scribeId) {
        // Candidate requests for exam
        Instant start = LocalDateTime.now().plusDays(2)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = start.plus(2, HOURS);

        return new RequestCreationModel(scribeId, CANDIDATE, new Date(start.toEpochMilli()), new Date(end.toEpochMilli()),
                LANGUAGE_ID, ADDRESS_ID, EXAM_ID);

    }

    private Request createRequest(String scribeId) {
        try {
            RequestCreationModel requestCreationModel = createRequestUpdate(scribeId);
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

    private void scribeRejectsRequest(Request request) throws Exception {
        String content = mapper.writeValueAsString(new RequestUpdateModel(REJECTED));
        mockMvc.perform(patch(SINGLE, request.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

}