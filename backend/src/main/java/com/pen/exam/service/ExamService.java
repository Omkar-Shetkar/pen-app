package com.pen.exam.service;

import com.pen.client.exam.ExamUpdate;
import com.pen.exam.domain.Exam;
import com.pen.exam.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamService {

    private ExamRepository examRepository;

    ExamService(ExamRepository examRepository){
        this.examRepository = examRepository;
    }

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