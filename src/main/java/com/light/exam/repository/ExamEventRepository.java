package com.light.exam.repository;

import com.light.exam.domain.ExamEvent;
import org.springframework.data.repository.CrudRepository;

public interface ExamEventRepository extends CrudRepository<ExamEvent, Long> {
}
