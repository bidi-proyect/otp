package com.bidi.otp.service;

import org.springframework.stereotype.Service;

@Service
public interface ValidateOtpService {

    void validateOtp(String otpCode);
}
