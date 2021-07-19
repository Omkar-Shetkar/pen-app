package com.pen.client.exam;

import com.pen.exam.domain.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamEventSearchFilter {

    public static final long UNASSIGNED_ID = 0;

    private String candidate;

    private String scribe;

    private Integer scheduledInNextHours;

    private ExamStatus examStatus;

}
