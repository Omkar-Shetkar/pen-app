package com.light.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.client.register.UserResource;
import com.light.user.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetUserResource() throws Exception {
        String response = mockMvc.perform(get(UserController.SINGLE, "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResource userResource = mapper.readValue(response, UserResource.class);
        System.out.println(userResource);
        Assertions.assertEquals("scribe1-fn", userResource.getFirstName());
    }

}
