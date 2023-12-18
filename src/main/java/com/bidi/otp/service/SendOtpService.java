package com.bidi.otp.service;

import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.util.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface SendOtpService {
    MessageResponse sendOtp(OtpRequestDto otpRequestDto);
}
