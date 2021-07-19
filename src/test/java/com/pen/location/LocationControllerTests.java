package com.pen.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pen.BaseTestSetup;
import com.pen.location.domain.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.pen.location.controller.LocationController.CITIES;


public class LocationControllerTests extends BaseTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getCities() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(CITIES))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<City> cities = mapper.readValue(response, List.class);

        Assertions.assertEquals(4, cities.size());
    }
}
