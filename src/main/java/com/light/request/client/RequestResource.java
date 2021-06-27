package com.light.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResource {
    private Long id;
    private ScribeResource scribeResource;
    private CandidateResource candidateResource;
    private ExamResource examResource;
}
