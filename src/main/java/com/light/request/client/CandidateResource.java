package com.light.request.client;

import com.light.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResource {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phone;
}