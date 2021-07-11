package com.light.otp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.BaseTestSetup;
import com.light.client.otp.OtpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static com.light.otp.controller.OtpController.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OtpControllerTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testCreate() throws Exception {
        OtpRequest otpRequest = new OtpRequest("1", "1");
        String content = mapper.writeValueAsString(otpRequest);
        mockMvc.perform(
                post(SINGLE)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    void testVerify() throws Exception {
        // Create OTP
        OtpRequest otpRequest = new OtpRequest("1", "1");
        String content = mapper.writeValueAsString(otpRequest);
        String response = mockMvc.perform(
                post(SINGLE)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "kn-IN")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer otp = mapper.readValue(response, Integer.class);

        // Verify OTP for success
        response = mockMvc.perform(get(VERIFY)
                .contentType(APPLICATION_JSON)
                .param("candidateId", "1")
                .param("scribeId", "1")
                .param("otp", String.valueOf(otp)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(SUCCESS, response);

        // Verify OTP for failure
        response = mockMvc.perform(get(VERIFY)
                .contentType(APPLICATION_JSON)
                .param("candidateId", "1")
                .param("scribeId", "1")
                .param("otp", String.valueOf("123456")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(FAILED, response);
    }
}
