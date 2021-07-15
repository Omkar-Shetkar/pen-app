package com.light.location.controller;

import com.light.location.domain.City;
import com.light.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/location")
public class LocationController {

    public final static String CITIES = "/rest/location/cities";

    @Autowired
    private LocationService locationService;

    @GetMapping("/cities")
    public ResponseEntity<Iterable<City>> getCities() {
        return ResponseEntity.ok(locationService.getCities());
    }
}
