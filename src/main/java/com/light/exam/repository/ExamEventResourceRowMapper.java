package com.light.exam.repository;

import com.light.domain.Gender;
import com.light.exam.domain.ExamStatus;
import com.light.client.request.CandidateResource;
import com.light.client.request.ExamEventResource;
import com.light.client.request.ExamResource;
import com.light.client.request.ScribeResource;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ExamEventResourceRowMapper implements RowMapper<ExamEventResource> {
    @Override
    public ExamEventResource mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        ExamStatus examStatus = ExamStatus.valueOf(resultSet.getString("exam_status"));
        CandidateResource candidateResource = new CandidateResource(resultSet.getString("c_first_name"),
                resultSet.getString("c_last_name"), Gender.valueOf(resultSet.getString("c_gender")),
                resultSet.getString("c_phone"));
        ScribeResource scribeResource = new ScribeResource(resultSet.getString("s_first_name"),
                resultSet.getString("s_last_name"), Gender.valueOf(resultSet.getString("s_gender")),
                resultSet.getString("s_phone"), resultSet.getString("s_email"));
        ExamResource examResource = new ExamResource(resultSet.getString("exam"),
                resultSet.getTimestamp("start_time"), resultSet.getTimestamp("end_time"),
                resultSet.getString("lang")/*, resultSet.getString("city")*/);

        return new ExamEventResource(resultSet.getLong("id"), examStatus, scribeResource, candidateResource, examResource);
    }
}
