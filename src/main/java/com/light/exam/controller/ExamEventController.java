package com.light.exam.controller;

import com.light.exam.client.ExamEventSearchFilter;
import com.light.exam.client.FeedbackDetails;
import com.light.exam.domain.ExamEvent;
import com.light.exam.domain.ExamStatus;
import com.light.exam.service.ExamEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/rest/exams/events")
public class ExamEventController {

    public static final String SINGLE = "/rest/exams/events/{id}";

    public static final String ALL = "/rest/exams/events";

    public static final String VERIFY = "/rest/exams/events/{id}/otp/verify";

    public static final String FEEDBACK = "/rest/exams/events/{id}/feedback";

    @Autowired
    private ExamEventService examEventService;

    @Autowired
    private com.light.otp.service.OtpService otpService;

    @GetMapping("/{id}")
    public ResponseEntity<ExamEvent> get(@PathVariable long id) {
        ExamEvent examEvent = examEventService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Exam event with id {%d} not found!", id)));

        return ResponseEntity.ok(examEvent);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody long requestId) {
        examEventService.delete(requestId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<ExamEvent>> search(@Validated ExamEventSearchFilter searchFilter) {
        List<ExamEvent> examEvents = examEventService.search(searchFilter);
        return ResponseEntity.ok(examEvents);
    }

    @PatchMapping("/{id}/feedback")
    public ResponseEntity<ExamEvent> updateFeedback(@PathVariable long id,
                                                    @Validated @RequestBody FeedbackDetails feedbackDetails) {
        try {
            examEventService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format("Exam event with id {%d} not found!", id)));
            examEventService.updateFeedback(id, feedbackDetails);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}/otp/verify")
    public ResponseEntity<String> verifyOtp(@PathVariable long id, @Validated com.light.otp.client.OtpRequest otpRequest) {
        try {
            ExamEvent examEvent = examEventService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format("Exam event with id {%d} not found!", id)));

            if (otpService.validOtp(otpRequest)) {
                examEvent.setOtpVerified(true);
                examEvent.setExamStatus(ExamStatus.STARTED);
                examEventService.save(examEvent);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}