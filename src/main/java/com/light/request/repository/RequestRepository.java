package com.light.request.repository;

import com.light.exam.domain.ExamSchedule;
import com.light.request.domain.Request;
import com.light.request.domain.RequestStatus;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Request r" +
            " WHERE r.examSchedule = :examSchedule " +
            "AND r.status != :status")
    void deleteByExamScheduleAndNotStatus(ExamSchedule examSchedule, RequestStatus status);

    @Query("""
            SELECT * FROM request r WHERE 
            r.scribe = :scribe AND  
            r.status = :status AND 
            r.exam_schedule = :examSchedule""")
    List<Request> search(Long scribe, Long examSchedule, RequestStatus status);
}
