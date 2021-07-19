package com.pen.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * An exam event represents scheduled exam for a scribe and candidate.
 * It is created when a scribe accepts candidate request for exam.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamEvent {

    @Id
    private Long id;

    private Long request;

    private String scribe;

    private String candidate;

    private Boolean otpGenerated = false;

    private Boolean otpVerified = false;

    private ExamStatus examStatus = ExamStatus.NOT_STARTED;

    private ScribeFeedback scribeFeedback;

    private CandidateFeedback candidateFeedback;

    private Long examSchedule;

    public ExamEvent(final Long request, final String candidate, final String scribe, final Long examSchedule) {
        this.request = request;
        this.scribe = scribe;
        this.candidate = candidate;
        this.examSchedule = examSchedule;
    }

}
