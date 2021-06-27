package com.light.request.repository;

import com.light.request.client.RequestResource;
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
           "r.exam_schedule = :examSchedule " +
           "AND r.status != :status")
    void deleteByExamScheduleAndNotStatus(Long examSchedule, RequestStatus status);

    @Query("""
            SELECT * FROM request r 
            WHERE 
            (:scribe IS NULL OR r.scribe = :scribe) AND
            (:candidate IS NULL OR r.candidate = :candidate) AND  
            (:examSchedule IS NULL OR r.exam_schedule = :examSchedule) AND  
            (:status IS NULL OR r.status = :status)
            """)
    List<Request> searchRequests(Long candidate, Long scribe, Long examSchedule, RequestStatus status);

    @Query(value = """
              SELECT r.id id, r.status status, 
              c.first_name c_first_name, c.last_name c_last_name, c.gender c_gender, c.phone c_phone,
              s.first_name s_first_name, s.last_name s_last_name, s.gender s_gender, s.email s_email, s.phone s_phone,
                    es.start_time start_time, es.end_time end_time, e.name exam,
                    l.name lang, a.city city
                FROM request r
            JOIN candidate c ON r.candidate=c.id
            JOIN scribe s ON r.scribe=s.id
            JOIN exam_schedule es ON r.exam_schedule=es.id
            JOIN exam e ON e.id=es.exam
            JOIN language l ON l.id=es.language
            JOIN address a ON a.id=es.address
               WHERE
                     (:scribe IS NULL OR r.scribe = :scribe) AND
                     (:candidate IS NULL OR r.candidate = :candidate) AND
                     (:examSchedule IS NULL OR r.exam_schedule = :examSchedule) AND
                     (:status IS NULL OR r.status = :status)
            """,
            rowMapperClass = RequestResourceRowMapper.class)
    List<RequestResource> searchRequestResources(Long candidate, Long scribe, Long examSchedule, RequestStatus status);
}
