package com.pen.client.request;

import com.pen.client.register.UserResource;
import com.pen.request.domain.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResource {
    private Long id;
    private RequestStatus status;
    private UserResource scribeResource;
    private UserResource candidateResource;
    private ExamResource examResource;
}
