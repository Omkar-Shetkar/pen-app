package com.pen.location.service;

import com.pen.location.domain.City;
import com.pen.location.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final CityRepository cityRepository;

    public LocationService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Iterable<City> getCities() {
        return cityRepository.findAll();
    }
}
