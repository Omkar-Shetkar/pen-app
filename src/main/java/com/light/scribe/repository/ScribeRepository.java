package com.light.scribe.repository;

import com.light.scribe.domain.Scribe;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

public interface ScribeRepository extends CrudRepository<Scribe, Long> {

    @Transactional(readOnly = true)
    @Query("""
            SELECT * FROM scribe s 
            INNER JOIN availability a ON a.scribe = s.id 
            INNER JOIN scribe_language l ON l.scribe = s.id 
            WHERE a.available_from <= :from 
            AND a.available_to >= :to 
            AND l.language = :language 
            """)
    Set<Scribe> findByAvailabilityAndLanguage(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
                                              @Param("language") Long language);
}
