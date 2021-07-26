package com.pen.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pen.BaseTestSetup;
import com.pen.client.request.RequestCreationModel;
import com.pen.client.request.RequestResource;
import com.pen.client.request.RequestUpdateModel;
import com.pen.exam.domain.ExamSchedule;
import com.pen.request.domain.Request;
import com.pen.request.domain.RequestStatus;
import com.pen.request.repository.ExamScheduleRepository;
import com.pen.scribe.controller.ScribeController;
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
    public static final String SCRIBE = "1";
    public static final long EXAM_ID = 101;
    public static final long LANGUAGE_ID = 1;
    public static final String ADDRESS = "venue-1";
    public static final long CITY_ID = 1L;

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

        // Scribe tries to accept the request again and should assert it as BAD REQUEST
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
        Assertions.assertEquals(3, requestSet.size());
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
        Assertions.assertEquals(3, requestSet.size());
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

        /**
         * Scribe gets a request and accepts it.
         */
        Instant start = LocalDateTime.of(2099, 10, 1, 6, 30)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = LocalDateTime.of(2099, 10, 1, 6, 45)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Request request = createRequest(SCRIBE, start, end);

        // Scribe accepts the request
        String content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
        String response = mockMvc.perform(patch(SINGLE, request.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        request = mapper.readValue(response, Request.class);
        assertEquals(ACCEPTED, request.getStatus());

        /**
         * Scribe gets another request for the same time slot and should not be able to accept it.
         */
        Request anotherRequest = createRequest(SCRIBE, start, end);

        // Scribe accepts the request
        content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
        mockMvc.perform(patch(SINGLE, anotherRequest.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());

        // Now, scribe should have only one request in ACCEPTED status
        response = mockMvc.perform(get((ALL))
                .param("scribe", String.valueOf(SCRIBE))
                .param("requestStatus", ACCEPTED.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<LinkedHashMap<String, String>> requestSet = mapper.readValue(response, List.class);
        Assertions.assertEquals(1, requestSet.size());
        Assertions.assertEquals(String.valueOf(request.getId()), String.valueOf(requestSet.iterator().next().get("id")));
    }

    /**
     * Overlapping but not exactly equal time schedules should be different.
     */
    @Test
    void testOverlappingExamSchedules() {
        /**
         * Scribe gets a request and accepts it.
         */
        Instant start = LocalDateTime.of(2099, 10, 1, 6, 30)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = LocalDateTime.of(2099, 10, 1, 6, 45)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Request request = createRequest(SCRIBE, start, end);
        Iterable<ExamSchedule> examSchedules = examScheduleRepository.findAll();

        // Expect one exam schedule
        if (examSchedules instanceof List examScheduleList) {
            assertEquals(1, examScheduleList.size());
        }

        start = LocalDateTime.of(2099, 10, 1, 6, 30)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        end = LocalDateTime.of(2099, 10, 1, 6, 35)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        request = createRequest(SCRIBE, start, end);
        examSchedules = examScheduleRepository.findAll();

        // Expect two exam schedules
        if (examSchedules instanceof List examScheduleList) {
            assertEquals(2, examScheduleList.size());
        }

    }

    /**
     * Scribes who are already writing exam for the given time slot, should not be listed in search results.
     */
    @Test
    void alreadyAssignedScribesShouldNotBeListed() throws Exception {

        /**
         * Search for scribes for the given time schedule.
         * Since scribe is free during the given time schedule, will be listed in search results.
         */
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Instant start = LocalDateTime.of(2099, 10, 1, 6, 30)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = LocalDateTime.of(2099, 10, 1, 6, 45)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        String response = mockMvc.perform(get(ScribeController.ALL)
                .contentType(APPLICATION_JSON)
                .param("from", format.format(new Date(start.toEpochMilli())))
                .param("to", format.format(new Date(end.toEpochMilli())))
                .param("language", String.valueOf(1L))
                .param("city", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Set<Scribe> result = mapper.readValue(response, Set.class);
        Assertions.assertEquals(2, result.size());


        /**
         * Scribe gets a request and accepts it.
         */

        Request request = createRequest(SCRIBE, start, end);

        // Scribe accepts the request
        String content = mapper.writeValueAsString(new RequestUpdateModel(ACCEPTED));
        response = mockMvc.perform(patch(SINGLE, request.getId())
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        request = mapper.readValue(response, Request.class);
        assertEquals(ACCEPTED, request.getStatus());

        /**
         * Search for scribes for the given time schedule.
         * Since scribe has already writing for the given time schedule, won't be listed in search results.
         */

        response = mockMvc.perform(get(ScribeController.ALL)
                .contentType(APPLICATION_JSON)
                .param("from", format.format(new Date(start.toEpochMilli())))
                .param("to", format.format(new Date(end.toEpochMilli())))
                .param("language", String.valueOf(1L))
                .param("city", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        result = mapper.readValue(response, Set.class);
        Assertions.assertEquals(1, result.size());
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

        return createRequestUpdate(scribeId, start, end);
    }

    private RequestCreationModel createRequestUpdate(String scribeId, Instant start, Instant end) {
        return new RequestCreationModel(scribeId, CANDIDATE,
                new Date(start.toEpochMilli()), new Date(end.toEpochMilli()),
                LANGUAGE_ID, EXAM_ID, ADDRESS, CITY_ID);
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

    private Request createRequest(String scribeId, Instant start, Instant end) {
        try {
            RequestCreationModel requestCreationModel = createRequestUpdate(scribeId, start, end);
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