package com.light.client.register;

import com.light.client.auth.UserCredentials;
import com.light.client.exam.UserType;
import com.light.domain.Gender;
import com.light.scribe.domain.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResource {

    private UserType userType;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String mobile;
    private String email;
    private Long city;
    private String location;
    private Set<Long> languages;
    private Availability availability;
    private UserCredentials credentials;

}
