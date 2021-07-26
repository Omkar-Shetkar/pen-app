package com.pen.scribe.repository;

import com.pen.scribe.domain.Scribe;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Repository
public interface ScribeRepository extends CrudRepository<Scribe, String>, CustomScribeRepository {

    @Transactional(readOnly = true)
    @Query("""
            SELECT * FROM scribe s 
            INNER JOIN scribe_language l ON l.scribe = s.id 
            WHERE 
            l.language = :language 
            AND s.city = :city
            AND s.id NOT IN
            (
               SELECT scribe FROM request r
               INNER JOIN exam_schedule es ON es.id = r.exam_schedule
               WHERE r.scribe = s.id
               AND (r.status = 'ACCEPTED' OR (r.status = 'PENDING' AND r.candidate = :candidate))
               AND 
                (DATE_TRUNC('MINUTE',es.start_Time) <= DATE_TRUNC('MINUTE',:to::timestamp) 
                AND DATE_TRUNC('MINUTE',es.end_time) >= DATE_TRUNC('MINUTE',:from::timestamp))
            )
            """)
        //TODO: Availability need to be considered
    Set<Scribe> findByAvailabilityAndLanguage(@Param("candidate") String candidate,
                                              @Param("from") Date from, @Param("to") Date to,
                                              @Param("language") Long language, @Param("city") Long city);
}
