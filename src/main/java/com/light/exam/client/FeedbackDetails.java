package com.light.exam.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDetails {

    private long userId;

    private UserType userType;

    private int rating;

    private String comment;
}
