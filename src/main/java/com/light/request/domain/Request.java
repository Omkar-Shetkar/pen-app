package com.light.request.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Candidate sends request to Scribe for exam writing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    private Long id;

    private String scribe;

    private String candidate;

    private Long examSchedule;

    private RequestStatus status;

    public Request(String scribe, String candidate, Long examSchedule) {
        this.scribe = scribe;
        this.candidate = candidate;
        this.examSchedule = examSchedule;
        this.status = RequestStatus.PENDING;
    }

}
