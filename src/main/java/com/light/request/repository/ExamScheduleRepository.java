package com.light.request.repository;

import com.light.exam.domain.ExamSchedule;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Optional;

public interface ExamScheduleRepository extends CrudRepository<ExamSchedule, Long> {

    Optional<ExamSchedule> findByStartTimeAndEndTime(Instant start, Instant end);

    @Query("""
            SELECT * FROM exam_schedule e 
            WHERE 
            e.exam = :exam AND 
            e.language = :language AND 
            (DATE_TRUNC('MINUTE',e.start_Time) <= DATE_TRUNC('MINUTE',:endTime) 
                AND DATE_TRUNC('MINUTE',e.end_time) >= DATE_TRUNC('MINUTE',:startTime))
            """)
    Optional<ExamSchedule> find(Long exam, Instant startTime, Instant endTime, Long language);
}
