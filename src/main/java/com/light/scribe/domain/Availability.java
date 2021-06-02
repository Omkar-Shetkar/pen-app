package com.light.scribe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Availability {

    private LocalDateTime availableFrom;

    private LocalDateTime availableTo;

}