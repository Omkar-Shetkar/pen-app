package com.pen.client.request;

import com.pen.client.register.UserResource;
import com.pen.exam.domain.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamEventResource {
    private Long id;
    private ExamStatus examStatus;
    private UserResource scribeResource;
    private UserResource candidateResource;
    private ExamResource examResource;
}
