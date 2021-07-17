package com.light.candidate.domain;

import com.light.client.exam.UserType;
import com.light.client.register.UserResource;
import com.light.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Candidate {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String mobile;

    public UserResource asUserResource() {
        return new UserResource(UserType.CANDIDATE, firstName, lastName, gender, mobile, null, null,
                null, null, null, null);
    }
}
