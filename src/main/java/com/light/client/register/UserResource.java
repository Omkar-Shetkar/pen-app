package com.light.client.register;

import com.light.candidate.domain.Candidate;
import com.light.client.auth.UserCredentials;
import com.light.client.exam.UserType;
import com.light.domain.Gender;
import com.light.scribe.domain.Availability;
import com.light.scribe.domain.Scribe;
import com.light.scribe.domain.ScribeLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResource {

    private UserType userType;
    private String id;
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


    public Scribe asScribe() {
        Set<ScribeLanguage> languageSet = languages.stream().map(l -> new ScribeLanguage(l))
                .collect(Collectors.toSet());
        return Scribe.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .mobile(mobile)
                .email(email)
                .city(city)
                .languages(languageSet)
                .availability(availability)
                .build();

    }

    public Candidate asCandidate() {
        return Candidate.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .mobile(mobile)
                .email(email)
                .build();
    }
}
