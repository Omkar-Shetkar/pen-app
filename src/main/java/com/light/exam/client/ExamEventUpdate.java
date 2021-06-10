package com.light.exam.client;

import com.light.request.domain.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamEventUpdate {

    private long requestId;

    private long scribe;

    private long candidate;

    private long examSchedule;

    public ExamEventUpdate(Request request) {
        this.requestId = request.getId();
        this.scribe = request.getScribe();
        this.candidate = request.getCandidate();
        this.examSchedule = request.getExamSchedule();
    }
}
