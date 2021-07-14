package com.light.scribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.client.scribe.ScribeUpdate;
import com.light.domain.Gender;
import com.light.scribe.domain.Availability;
import com.light.scribe.domain.Scribe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static com.light.scribe.controller.ScribeController.ALL;
import static com.light.scribe.controller.ScribeController.UPDATE;
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
        LocalDateTime from = LocalDateTime.of(2020, 12, 26, 06, 00);
        LocalDateTime to = LocalDateTime.of(2020, 12, 26, 10, 00);

        String response = mockMvc.perform(get(ALL)
                .contentType(APPLICATION_JSON)
                .param("from", from.format(DateTimeFormatter.ISO_DATE_TIME))
                .param("to", to.format(DateTimeFormatter.ISO_DATE_TIME))
                .param("language", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Set<Scribe> result = mapper.readValue(response, Set.class);
        Assertions.assertEquals(2, result.size());
    }

}
