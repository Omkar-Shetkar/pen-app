package com.pen.client.register;

import com.pen.candidate.domain.Candidate;
import com.pen.client.auth.UserCredentials;
import com.pen.client.exam.UserType;
import com.pen.domain.Gender;
import com.pen.scribe.domain.Availability;
import com.pen.scribe.domain.Scribe;
import com.pen.scribe.domain.ScribeLanguage;
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
