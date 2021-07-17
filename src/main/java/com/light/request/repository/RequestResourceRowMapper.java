package com.light.request.repository;

import com.light.client.exam.UserType;
import com.light.client.register.UserResource;
import com.light.client.request.ExamResource;
import com.light.client.request.RequestResource;
import com.light.domain.Gender;
import com.light.request.domain.RequestStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestResourceRowMapper implements RowMapper<RequestResource> {
    @Override
    public RequestResource mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        RequestStatus status = RequestStatus.valueOf(resultSet.getString("status"));

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
                .build();

        return new RequestResource(resultSet.getLong("id"), status, scribeResource, candidateResource, examResource);
    }
}
