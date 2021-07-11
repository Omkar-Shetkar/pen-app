package com.light.client.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDetails {

    private String userId;

    private UserType userType;

    private int rating;

    private String comment;
}
