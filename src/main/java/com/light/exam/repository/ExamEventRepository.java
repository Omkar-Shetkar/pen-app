package com.light.exam.repository;

import com.light.exam.domain.ExamEvent;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ExamEventRepository extends CrudRepository<ExamEvent, Long> {
    Optional<ExamEvent> findByRequest(long request);

    @Query("""
            SELECT * FROM exam_event e
            WHERE
            ((:candidate IS NULL OR e.candidate = :candidate) OR
            (:scribe IS NULL OR e.scribe = :scribe)) OR
            e.exam_schedule IN
            (
                SELECT id FROM exam_schedule es 
                WHERE
                :scheduledInNextHours IS NOT NULL AND
                extract(epoch from es.start_time - CURRENT_TIMESTAMP)/3600 <= :scheduledInNextHours 
            )
            """)
    List<ExamEvent> search(Long candidate, Long scribe, Integer scheduledInNextHours);

    @Query("""
            SELECT * FROM exam_event e
            WHERE
            e.exam_status = 'STARTED' AND
            e.exam_schedule IN
            (
                SELECT id FROM exam_schedule es
                WHERE
                es.end_time > now()::timestamp
                        
            )
            """)
    List<ExamEvent> getAllExamsEligibleForCompletion();
}
