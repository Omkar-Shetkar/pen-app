package com.light.otp.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    private long candidateId;
    private long scribeId;
    private int otp;

    public OtpRequest(long candidateId, long scribeId) {
        this.candidateId = candidateId;
        this.scribeId = scribeId;
    }
}
