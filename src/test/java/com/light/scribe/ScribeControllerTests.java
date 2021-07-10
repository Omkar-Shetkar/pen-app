package com.light.scribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.domain.Gender;
import com.light.scribe.client.ScribeUpdate;
import com.light.scribe.domain.Availability;
import com.light.scribe.domain.Scribe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.light.scribe.controller.ScribeController.ALL;
import static com.light.scribe.controller.ScribeController.UPDATE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        Instant from = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        Instant to = from.plus(2, ChronoUnit.DAYS);
        Availability availability = new Availability(new Date(from.toEpochMilli()), new Date(to.toEpochMilli()));
        Set<Availability> availabilities = new HashSet<>();
        availabilities.add(availability);
        ScribeUpdate scribeUpdate = new ScribeUpdate(availabilities);

        mockMvc.perform((put(UPDATE, "1")
                .with(csrf()).contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(scribeUpdate))))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Updating scribe details with wrong scribe - expect HTTP 404")
    void updateScribeAvailabilityWithWrongScribe() throws Exception {
        Instant from = Instant.now().plus(1, ChronoUnit.DAYS);
        Instant to = from.plus(2, ChronoUnit.DAYS);
        Availability availability = new Availability(new Date(from.toEpochMilli()), new Date(to.toEpochMilli()));
        Set<Availability> availabilities = new HashSet<>();
        availabilities.add(availability);
        ScribeUpdate scribeUpdate = new ScribeUpdate(availabilities);

        mockMvc.perform((put(UPDATE, "100")
                .with(csrf()).contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(scribeUpdate))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Find scribes by exam schedule and language")
    void findScribesByExamSchedule() throws Exception {
        LocalDateTime from = LocalDateTime.of(2020, 12, 26, 06, 00);
        LocalDateTime to = LocalDateTime.of(2020, 12, 26, 10, 00);

        String response = mockMvc.perform(get(ALL)
                .with(csrf()).contentType(APPLICATION_JSON)
                .param("from", from.format(DateTimeFormatter.ISO_DATE_TIME))
                .param("to", to.format(DateTimeFormatter.ISO_DATE_TIME))
                .param("language", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Set<Scribe> result = mapper.readValue(response, Set.class);
        Assertions.assertEquals(2, result.size());
    }

}
