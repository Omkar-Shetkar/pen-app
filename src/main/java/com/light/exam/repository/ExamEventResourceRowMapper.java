package com.light.exam.repository;

import com.light.client.exam.UserType;
import com.light.client.register.UserResource;
import com.light.client.request.ExamEventResource;
import com.light.client.request.ExamResource;
import com.light.domain.Gender;
import com.light.exam.domain.ExamStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ExamEventResourceRowMapper implements RowMapper<ExamEventResource> {
    @Override
    public ExamEventResource mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        ExamStatus examStatus = ExamStatus.valueOf(resultSet.getString("exam_status"));

        UserResource candidateResource = new UserResource(UserType.CANDIDATE, resultSet.getString("c_first_name"),
                resultSet.getString("c_last_name"), Gender.valueOf(resultSet.getString("c_gender")),
                resultSet.getString("c_mobile"), null, null, null,
                null, null, null);

        UserResource scribeResource = new UserResource(UserType.SCRIBE, resultSet.getString("s_first_name"),
                resultSet.getString("s_last_name"), Gender.valueOf(resultSet.getString("s_gender")),
                resultSet.getString("s_mobile"), resultSet.getString("s_email"), null, null,
                null, null, null);

        ExamResource examResource = new ExamResource(resultSet.getString("exam"),
                resultSet.getTimestamp("start_time"), resultSet.getTimestamp("end_time"),
                resultSet.getString("lang")/*, resultSet.getString("city")*/);

        return new ExamEventResource(resultSet.getLong("id"), examStatus, scribeResource,
                candidateResource, examResource);
    }
}
