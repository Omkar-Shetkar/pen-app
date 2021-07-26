package com.pen.scribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pen.BaseTestSetup;
import com.pen.client.scribe.ScribeUpdate;
import com.pen.domain.Gender;
import com.pen.scribe.domain.Availability;
import com.pen.scribe.domain.Scribe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static com.pen.scribe.controller.ScribeController.ALL;
import static com.pen.scribe.controller.ScribeController.UPDATE;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ScribeControllerTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    public static final String CANDIDATE = "100";

    //    @Test
    @Disabled("To be done later")
    void createScribe() throws Exception {
        mockMvc.perform(post(ALL)).andExpect(status().isCreated());

        mockMvc.perform(post(ALL).param("firstName", "user1-fn")
                .param("lastName", "user1-ln").param("gender", Gender.MALE.toString())
                .param("mobile", "1122334455"))
                .andExpect(status().isCreated());
    }

    @Test
    void updateScribeAvailability() throws Exception {
        ScribeUpdate scribeUpdate = new ScribeUpdate(Availability.WEEKDAYS);

        mockMvc.perform((put(UPDATE, "1")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(scribeUpdate))))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Updating scribe details with wrong scribe - expect HTTP 404")
    void updateScribeAvailabilityWithWrongScribe() throws Exception {
        ScribeUpdate scribeUpdate = new ScribeUpdate(Availability.ANY_DAY);

        mockMvc.perform((put(UPDATE, "100")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(scribeUpdate))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Find scribes by exam schedule and language")
    void findScribesByExamSchedule() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Instant start = LocalDateTime.now().plusDays(2)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Instant end = start.plus(2, HOURS);

        String response = mockMvc.perform(get(ALL)
                .contentType(APPLICATION_JSON)
                .param("candidate", String.valueOf(CANDIDATE))
                .param("from", format.format(new Date(start.toEpochMilli())))
                .param("to", format.format(new Date(end.toEpochMilli())))
                .param("language", String.valueOf(1L))
                .param("city", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Set<Scribe> result = mapper.readValue(response, Set.class);
        Assertions.assertEquals(2, result.size());
    }

}
