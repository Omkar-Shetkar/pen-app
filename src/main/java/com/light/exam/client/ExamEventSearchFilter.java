package com.light.exam.client;

import com.light.exam.domain.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamEventSearchFilter {

    public static final long UNASSIGNED_ID = 0;

    private Long candidate;

    private Long scribe;

    private Integer scheduledInNextHours;

    private ExamStatus examStatus;

}
