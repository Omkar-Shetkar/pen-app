package com.light.exam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/exams/events")
public class ExamEventController {

    public static final String SINGLE = "/rest/exams/events/{id}";

    public static final String ALL = "/rest/exams/events";

    public static final String VERIFY = "/rest/exams/events/{id}/otp/verify";

    public static final String FEEDBACK = "/rest/exams/events/{id}/feedback";
}