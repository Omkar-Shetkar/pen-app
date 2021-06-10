package com.light.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * An exam event represents scheduled exam for a scribe and candidate.
 * It is created when a scribe accepts candidate request for exam.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("EXAM_EVENT")
public class ExamEvent {

    @Id
    private Long id;

    private Long request;

    private Long scribe;

    private Long candidate;

    private Boolean otpGenerated;

    private Boolean otpVerified;

    private ExamStatus examStatus = ExamStatus.NOT_STARTED;

    private Feedback scribeFeedback;

    private Feedback candidateFeedback;

    private Long examSchedule;

    public ExamEvent(final Long request, final Long candidate, final Long scribe, final Long examSchedule) {
        this.request = request;
        this.scribe = scribe;
        this.candidate = candidate;
        this.examSchedule = examSchedule;
    }

}
