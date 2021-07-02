package com.light.request.repository;

import com.light.domain.Gender;
import com.light.request.client.CandidateResource;
import com.light.request.client.ExamResource;
import com.light.request.client.RequestResource;
import com.light.request.client.ScribeResource;
import com.light.request.domain.RequestStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestResourceRowMapper implements RowMapper<RequestResource> {
    @Override
    public RequestResource mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        RequestStatus status = RequestStatus.valueOf(resultSet.getString("status"));
        CandidateResource candidateResource = new CandidateResource(resultSet.getString("c_first_name"),
                resultSet.getString("c_last_name"), Gender.valueOf(resultSet.getString("c_gender")),
                resultSet.getString("c_phone"));
        ScribeResource scribeResource = new ScribeResource(resultSet.getString("s_first_name"),
                resultSet.getString("s_last_name"), Gender.valueOf(resultSet.getString("s_gender")),
                resultSet.getString("s_phone"), resultSet.getString("s_email"));
        ExamResource examResource = new ExamResource(resultSet.getString("exam"),
                resultSet.getTimestamp("start_time"), resultSet.getTimestamp("end_time"),
                resultSet.getString("lang")/*, resultSet.getString("city")*/);

        return new RequestResource(resultSet.getLong("id"), status, scribeResource, candidateResource, examResource);
    }
}
