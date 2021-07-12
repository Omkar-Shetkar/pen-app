package com.light.exam.repository;

import com.light.exam.domain.ExamEvent;
import com.light.exam.domain.ExamStatus;
import com.light.client.request.ExamEventResource;
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
    List<ExamEvent> searchExamEvents(String candidate, String scribe, Integer scheduledInNextHours);

    @Query(value = """
                        SELECT ev.id id, ev.exam_status exam_status, 
                              c.first_name c_first_name, c.last_name c_last_name, c.gender c_gender, c.mobile c_mobile,
                              s.first_name s_first_name, s.last_name s_last_name, s.gender s_gender, s.email s_email, s.mobile s_mobile,
                              es.start_time start_time, es.end_time end_time, e.name exam,
                              l.name lang
            --                a.city city 
                        FROM exam_event ev
                            JOIN candidate c ON ev.candidate=c.id
                            JOIN scribe s ON ev.scribe=s.id
                            JOIN exam_schedule es ON ev.exam_schedule=es.id
                            JOIN exam e ON e.id=es.exam
                            JOIN language l ON l.id=es.language
                --          JOIN address a ON a.id=es.address
                        WHERE
                        ((:candidate IS NULL OR ev.candidate = :candidate) AND
                        (:scribe IS NULL OR ev.scribe = :scribe)) AND
                        (:examStatus IS NULL OR ev.exam_status = :examStatus) AND
                        (:scheduledInNextHours IS NULL OR ev.exam_schedule IN
                            (
                                SELECT id FROM exam_schedule es 
                                WHERE
                                :scheduledInNextHours IS NOT NULL AND
                                extract(epoch from es.start_time - CURRENT_TIMESTAMP)/3600 <= :scheduledInNextHours 
                            )
                        )
                        """,
            rowMapperClass = ExamEventResourceRowMapper.class)
    List<ExamEventResource> searchExamEventResources(String candidate, String scribe, Integer scheduledInNextHours,
                                                     ExamStatus examStatus);

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
