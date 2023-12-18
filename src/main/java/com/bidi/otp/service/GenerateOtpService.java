package com.bidi.otp.service;

import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.dto.OtpResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface GenerateOtpService {
    OtpResponseDto generateOtpWithResponse (OtpRequestDto otpRequestDto);
    String generateOtp(OtpRequestDto otpRequestDto);
}
