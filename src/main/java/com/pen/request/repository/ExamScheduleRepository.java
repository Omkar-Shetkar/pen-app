package com.pen.request.repository;

import com.pen.exam.domain.ExamSchedule;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

public interface ExamScheduleRepository extends CrudRepository<ExamSchedule, Long> {

    @Query("""
            SELECT * FROM exam_schedule e 
            WHERE 
            e.exam = :exam AND 
            e.language = :language AND 
            (DATE_TRUNC('MINUTE',e.start_Time) <= DATE_TRUNC('MINUTE',:endTime::timestamp) 
                AND DATE_TRUNC('MINUTE',e.end_time) >= DATE_TRUNC('MINUTE',:startTime::timestamp))
            """)
    Optional<ExamSchedule> find(Long exam, Date startTime, Date endTime, Long language);
}
