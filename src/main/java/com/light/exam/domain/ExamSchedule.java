package com.light.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("EXAM_SCHEDULE")
public class ExamSchedule {

    @Id
    private Long id;

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

    public ExamSchedule(final Instant startTime, final Instant endTime, final Long language, final Long address,
                        final Long exam) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.address = address;
        this.exam = exam;
    }

}
