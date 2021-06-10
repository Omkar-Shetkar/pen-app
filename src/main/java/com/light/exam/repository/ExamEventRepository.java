package com.light.exam.repository;

import com.light.exam.domain.ExamEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExamEventRepository extends CrudRepository<ExamEvent, Long> {
    Optional<ExamEvent> findByRequest(long request);

}
