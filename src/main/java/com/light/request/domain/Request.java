package com.light.request.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Candidate sends request to Scribe for exam writing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("REQUEST")
public class Request {

    @Id
    private Long id;

    private Long scribe;

    private Long candidate;

    private Long examSchedule;

    private RequestStatus status;

    public Request(Long scribe, Long candidate, Long examSchedule) {
        this.scribe = scribe;
        this.candidate = candidate;
        this.examSchedule = examSchedule;
        this.status = RequestStatus.PENDING;
    }

}
