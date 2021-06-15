package com.light.otp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.light.otp.client.OtpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OtpService {

    private Random random = new SecureRandom();

    private LoadingCache<Integer, Integer> otpCache;

    public OtpService(@Value("${light.exam.otp.expire-in-ms}") long expiresInMs) {
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiresInMs, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public Integer load(Integer key) throws Exception {
                        return 0;
                    }
                });
    }

    public int generate(OtpRequest otpRequest) {
        int otp = 100000 + random.nextInt(900000);
        int hash = getHashOfOtpRequest(otpRequest);
        otpCache.put(hash, otp);
        log.info("OTP generated for {}", otpRequest);
        return otp;
    }

    public int get(OtpRequest otpRequest) throws ExecutionException {
        int hash = getHashOfOtpRequest(otpRequest);
        return otpCache.get(hash);
    }

    private int getHashOfOtpRequest(OtpRequest otpRequest) {
        return Objects.hash(otpRequest.getCandidateId(), otpRequest.getScribeId());
    }

    public boolean validOtp(OtpRequest otpRequest) throws ExecutionException {
        int otpInCache = get(otpRequest);
        return otpInCache == otpRequest.getOtp();
    }
}
