package com.pen.otp.controller;

import com.pen.client.otp.OtpRequest;
import com.pen.otp.service.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/rest/otp")
public class OtpController {

    public static final String SINGLE = "/rest/otp";
    public static final String VERIFY = "/rest/otp/verify";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    final private OtpService otpService;

    OtpController (OtpService otpService){
        this.otpService = otpService;
    }

    @PostMapping
    public ResponseEntity<Integer> create(@Validated @RequestBody OtpRequest otpRequest) {
       int otp = otpService.generate(otpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(otp);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyOtp(@Validated OtpRequest otpRequest) {
        try {
            return otpService.validOtp(otpRequest) ? ResponseEntity.ok(SUCCESS) : ResponseEntity.ok(FAILED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
