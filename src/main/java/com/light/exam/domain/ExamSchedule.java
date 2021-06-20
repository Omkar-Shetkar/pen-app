package com.light.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSchedule {

    @Id
    private Long id;

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

    public ExamSchedule(final Date startTime, final Date endTime, final Long language, final Long address,
                        final Long exam) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.address = address;
        this.exam = exam;
    }

}
