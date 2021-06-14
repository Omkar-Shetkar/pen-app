package com.light.request.repository;

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
            " WHERE " +
            "r.examSchedule = :examSchedule " +
            "AND r.status != :status")
    void deleteByExamScheduleAndNotStatus(Long examSchedule, RequestStatus status);

    @Query("""
            SELECT * FROM request r 
            WHERE 
            (r.scribe = :scribe OR r.candidate = :candidate) AND  
            (:examSchedule IS NULL OR :examSchedule = 0 OR r.exam_schedule = :examSchedule) AND  
            (:status IS NULL OR r.status = :status)
            """)
    List<Request> search(Long candidate, Long scribe, Long examSchedule, RequestStatus status);
}
