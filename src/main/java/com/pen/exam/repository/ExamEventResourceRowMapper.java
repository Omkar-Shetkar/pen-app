package com.pen.exam.repository;

import com.pen.client.exam.UserType;
import com.pen.client.register.UserResource;
import com.pen.client.request.ExamEventResource;
import com.pen.client.request.ExamResource;
import com.pen.domain.Gender;
import com.pen.exam.domain.ExamStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ExamEventResourceRowMapper implements RowMapper<ExamEventResource> {
    @Override
    public ExamEventResource mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        ExamStatus examStatus = ExamStatus.valueOf(resultSet.getString("exam_status"));

        UserResource candidateResource = UserResource.builder()
                .id(resultSet.getString("c_id"))
                .userType(UserType.CANDIDATE)
                .firstName(resultSet.getString("c_first_name"))
                .lastName(resultSet.getString("c_last_name"))
                .gender(Gender.valueOf(resultSet.getString("c_gender")))
                .mobile(resultSet.getString("c_mobile"))
                .build();

        UserResource scribeResource = UserResource.builder()
                .id(resultSet.getString("s_id"))
                .userType(UserType.SCRIBE)
                .firstName(resultSet.getString("s_first_name"))
                .lastName(resultSet.getString("s_last_name"))
                .gender(Gender.valueOf(resultSet.getString("s_gender")))
                .mobile(resultSet.getString("s_mobile"))
                .email(resultSet.getString("s_email"))
                .build();

        ExamResource examResource = ExamResource.builder()
                .name(resultSet.getString("exam"))
                .start(resultSet.getTimestamp("start_time"))
                .end(resultSet.getTimestamp("end_time"))
                .language(resultSet.getString("lang"))
                .city(resultSet.getString("city"))
                .address(resultSet.getString("address"))
                .build();

        return new ExamEventResource(resultSet.getLong("id"), examStatus, scribeResource,
                candidateResource, examResource);
    }
}
