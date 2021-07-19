package com.pen.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pen.BaseTestSetup;
import com.pen.domain.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.pen.language.controller.LanguageController.ALL;

class LanguageControllerTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getLanguages() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Language> languages = mapper.readValue(response, List.class);

        Assertions.assertEquals(5, languages.size());
    }

}
