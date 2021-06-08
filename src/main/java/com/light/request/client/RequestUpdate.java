package com.light.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdate {

    @NonNull
    private Long scribeId;

    @NonNull
    private Long candidateId;

    @NonNull
    private Instant startTime;

    @NonNull
    private Instant endTime;

    @NonNull
    private Long language;

    @NonNull
    private Long address;

    @NonNull
    private Long exam;
}
