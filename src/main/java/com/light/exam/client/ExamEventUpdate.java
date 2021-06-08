package com.light.exam.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamEventUpdate {

    private long scribe;

    private long candidate;

    private long examSchedule;

}
