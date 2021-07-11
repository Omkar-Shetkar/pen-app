package com.light.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreationModel {

    @NonNull
    private String scribeId;

    @NonNull
    private String candidateId;

    @NonNull
    private Date startTime;

    @NonNull
    private Date endTime;

    @NonNull
    private Long language;

    @NonNull
    private Long address;

    @NonNull
    private Long exam;
}
