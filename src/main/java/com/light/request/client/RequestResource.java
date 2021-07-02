package com.light.request.client;

import com.light.request.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResource {
    private Long id;
    private RequestStatus status;
    private ScribeResource scribeResource;
    private CandidateResource candidateResource;
    private ExamResource examResource;
}
