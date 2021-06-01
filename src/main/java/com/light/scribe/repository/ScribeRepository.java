package com.light.scribe.repository;

import com.light.scribe.domain.Scribe;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Set;

public interface ScribeRepository extends CrudRepository<Scribe, Long> {

    @Query("SELECT s FROM Scribe s" +
            " JOIN s.availabilities a" +
            " JOIN s.languages l" +
            " WHERE a.availableFrom <= :from AND a.availableTo >= :to" +
            " AND l.id = :language")
    Set<Scribe> findByAvailabilityAndLanguage(LocalDateTime from, LocalDateTime to, Long language);
}
