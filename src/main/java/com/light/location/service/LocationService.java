package com.light.location.service;

import com.light.location.domain.City;
import com.light.location.repository.CityRepository;
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
