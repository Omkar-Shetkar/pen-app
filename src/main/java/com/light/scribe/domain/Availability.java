package com.light.scribe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Availability {

    private Date availableFrom;

    private Date availableTo;

}