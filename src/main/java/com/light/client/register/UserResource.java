package com.light.client.register;

import com.light.client.auth.UserCredentials;
import com.light.client.exam.UserType;
import com.light.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String location;
    private String[] languages;
    private String availability;
    private UserCredentials credentials;

}
