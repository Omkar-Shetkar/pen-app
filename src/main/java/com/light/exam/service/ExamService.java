package com.light.exam.service;

import com.light.client.exam.ExamUpdate;
import com.light.exam.domain.Exam;
import com.light.exam.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public Exam save(ExamUpdate examUpdate) {
        var exam = new Exam(examUpdate.getExamName());
        return examRepository.save(exam);
    }

    public void delete(long id) {
        examRepository.deleteById(id);
    }

    public Optional<Exam> get(long id) {
        return examRepository.findById(id);
    }

    public Iterable<Exam> getAll() {
        return examRepository.findAll();
    }
}