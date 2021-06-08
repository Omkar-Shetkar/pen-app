package com.light.request.repository;

import com.light.exam.domain.ExamSchedule;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Optional;

public interface ExamScheduleRepository extends CrudRepository<ExamSchedule, Long> {

    Optional<ExamSchedule> findByStartTimeAndEndTime(Instant start, Instant end);
}
