package com.pen.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScribeFeedback {

    private int rating;

    private String comment;
}
