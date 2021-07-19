package com.pen.candidate.domain;

import com.pen.client.exam.UserType;
import com.pen.client.register.UserResource;
import com.pen.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@Builder
public class Candidate {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String mobile;

    private String email;

    public UserResource asUserResource() {
        return UserResource.builder()
                .userType(UserType.CANDIDATE)
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .mobile(mobile)
                .email(email)
                .build();
    }
}
