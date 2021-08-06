package com.pen.client.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    private String candidateId;
    private String scribeId;
    private int otp;

    public OtpRequest(String candidateId, String scribeId) {
        this.candidateId = candidateId;
        this.scribeId = scribeId;
    }
}
