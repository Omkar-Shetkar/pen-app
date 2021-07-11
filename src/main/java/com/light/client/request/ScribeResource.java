package com.light.client.request;

import com.light.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//FIXME: Merge ScribeResource and CandidateResource OR use UserResource
public class ScribeResource {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phone;
    private String email;
}
