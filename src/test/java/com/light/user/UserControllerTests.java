package com.light.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.client.exam.UserType;
import com.light.client.register.UserResource;
import com.light.user.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetUserResource() throws Exception {
        // Get a scribe
        String response = mockMvc.perform(get(UserController.SINGLE, "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResource userResource = mapper.readValue(response, UserResource.class);
        Assertions.assertEquals("scribe1-fn", userResource.getFirstName());
        Assertions.assertEquals(UserType.SCRIBE, userResource.getUserType());

        // Get a candidate
        response = mockMvc.perform(get(UserController.SINGLE, "100"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        userResource = mapper.readValue(response, UserResource.class);
        Assertions.assertEquals("candidate1-fn", userResource.getFirstName());
        Assertions.assertEquals(UserType.CANDIDATE, userResource.getUserType());
    }

    @Test
    public void testUpdateScribe() throws Exception {
        // Get a scribe
        String response = mockMvc.perform(get(UserController.SINGLE, "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResource userResource = mapper.readValue(response, UserResource.class);
        Assertions.assertEquals("scribe1-fn", userResource.getFirstName());

        userResource.setFirstName("Omkar");

        response = mockMvc.perform(post(UserController.SINGLE, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userResource))
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        userResource = mapper.readValue(response, UserResource.class);
        Assertions.assertEquals("Omkar", userResource.getFirstName());
    }

    @Test
    public void testUpdateCandidate() throws Exception {
        // Get a scribe
        String response = mockMvc.perform(get(UserController.SINGLE, "100"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResource userResource = mapper.readValue(response, UserResource.class);
        Assertions.assertEquals("candidate1-fn", userResource.getFirstName());

        userResource.setFirstName("Omkar");

        response = mockMvc.perform(post(UserController.SINGLE, "100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userResource))
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        userResource = mapper.readValue(response, UserResource.class);
        Assertions.assertEquals("Omkar", userResource.getFirstName());
    }

}
