package com.pen.scribe.repository;

import com.pen.scribe.domain.Scribe;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

public interface ScribeRepository extends CrudRepository<Scribe, String>, CustomScribeRepository {

    @Transactional(readOnly = true)
    @Query("""
            SELECT * FROM scribe s 
            INNER JOIN scribe_language l ON l.scribe = s.id 
            WHERE 
            l.language = :language AND s.city = :city
            """)
    //TODO: Availability need to be considered
    Set<Scribe> findByAvailabilityAndLanguage(@Param("from") Date from, @Param("to") Date to,
                                              @Param("language") Long language, @Param("city") Long city);
}
