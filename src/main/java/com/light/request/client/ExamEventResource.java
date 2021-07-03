package com.light.request.client;

import com.light.exam.domain.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamEventResource {
    private Long id;
    private ExamStatus examStatus;
    private ScribeResource scribeResource;
    private CandidateResource candidateResource;
    private ExamResource examResource;
}
