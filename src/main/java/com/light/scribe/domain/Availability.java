package com.light.scribe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Availability {

    @Id
    private long id;

    private LocalDateTime availableFrom;

    private LocalDateTime availableTo;

    public Availability(LocalDateTime from, LocalDateTime to) {
        this.availableFrom = from;
        this.availableTo = to;
    }
}