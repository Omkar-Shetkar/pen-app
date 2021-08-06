package com.pen.exam.controller;

import com.pen.client.exam.ExamUpdate;
import com.pen.exam.domain.Exam;
import com.pen.exam.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/rest/exams")
public class ExamController {

    public static final String ALL = "/rest/exams";
    public static final String SINGLE = "/rest/exams/{id}";

    final private ExamService examService;

    ExamController(ExamService examService){
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<Exam> create(@RequestBody ExamUpdate examUpdate) {
        var exam = examService.save(examUpdate);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(exam.getId()).toUri();
        return ResponseEntity.created(location).body(exam);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        try {
            examService.delete(id);
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> get(@PathVariable long id) {
        var exam = examService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Exam with id {%d} not found!", id)));

        return ResponseEntity.ok(exam);
    }

    @GetMapping
    public ResponseEntity<Iterable<Exam>> getAll() {
        Iterable<Exam> exams = examService.getAll();
        return ResponseEntity.ok(exams);
    }
}