package com.light.scribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.LanguageRepository;
import com.light.domain.Gender;
import com.light.scribe.client.ScribeUpdate;
import com.light.scribe.domain.Availability;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.light.scribe.controller.ScribeController.ALL;
import static com.light.scribe.controller.ScribeController.UPDATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ScribeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
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
        LocalDateTime from = LocalDateTime.now().plusDays(1);
        LocalDateTime to = from.plusDays(2);
        Availability availability = new Availability(from, to);
        Set<Availability> availabilities = new HashSet<>();
        availabilities.add(availability);
        ScribeUpdate scribeUpdate = new ScribeUpdate(availabilities);

        mockMvc.perform((put(UPDATE, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(scribeUpdate))))
                .andExpect(status().isCreated());
    }

}
