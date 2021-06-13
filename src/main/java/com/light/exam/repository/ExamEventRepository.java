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
            e.candidate = :candidate
            """)
    List<ExamEvent> search(Long candidate, Long scribe);
}
