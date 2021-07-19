package com.pen.exam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pen.BaseTestSetup;
import com.pen.client.exam.ExamUpdate;
import com.pen.exam.domain.Exam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.pen.exam.controller.ExamController.ALL;
import static com.pen.exam.controller.ExamController.SINGLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamControllerTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testCreateExam() throws Exception {

        ExamUpdate examUpdate = new ExamUpdate("SSLC");
        String content = mapper.writeValueAsString(examUpdate);
        String response = mockMvc.perform(post(ALL)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Exam exam = mapper.readValue(response, Exam.class);
        assertEquals("SSLC", exam.getName());

    }

    @Test
    void testExamDelete() throws Exception {

        // Assert count of exams
        String response = mockMvc.perform(get(ALL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Exam> exams = mapper.readValue(response, List.class);
        assertEquals(3, exams.size());

        // Delete an exam
        mockMvc.perform(delete(SINGLE, 101))
                .andExpect(status().isAccepted());

        // Assert count of exams
        response = mockMvc.perform(get(ALL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        exams = mapper.readValue(response, List.class);
        assertEquals(2, exams.size());
    }

    @Test
    public void getExams() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Exam> exams = mapper.readValue(response, List.class);

        Assertions.assertEquals(3, exams.size());
    }

}
