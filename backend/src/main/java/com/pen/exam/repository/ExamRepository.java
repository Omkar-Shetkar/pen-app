package com.pen.exam.repository;

import com.pen.exam.domain.Exam;
import org.springframework.data.repository.CrudRepository;

public interface ExamRepository extends CrudRepository<Exam, Long> {
}
