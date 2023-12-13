package com.bidi.otp.service;

import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.exception.ApiException;
import com.bidi.otp.util.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface OtpService {

    String generatedOtp(String idUser, String phoneNumber) throws ApiException;

    boolean sendOtpSms(String idUser, String phoneNumber) throws ApiException;

    boolean sendOtpEmail(String email, String userId);

    MessageResponse sendOtp(OtpRequestDto otpRequestDto, String userId) throws ApiException;

    boolean validateOtp(String otpCode);
}
