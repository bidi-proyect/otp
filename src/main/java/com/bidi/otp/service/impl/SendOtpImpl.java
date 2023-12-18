package com.bidi.otp.service.impl;

import com.bidi.otp.config.TwilioConfig;
import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.exception.ApiException;
import com.bidi.otp.service.EmailService;
import com.bidi.otp.service.GenerateOtpService;
import com.bidi.otp.service.SendOtpService;
import com.bidi.otp.util.MessageResponse;
import com.bidi.otp.util.OtpEncode;
import com.bidi.otp.util.StringConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendOtpImpl implements SendOtpService {
    @Value("${email.sender}")
    private String emailUser;

    private final GenerateOtpService generateOtpService;
    private final TwilioConfig twilioConfig;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(SendOtpImpl.class);


    @Override
    public MessageResponse sendOtp(OtpRequestDto otpRequestDto) {

        boolean point = false;
        String otpGenerated = generateOtpService.generateOtp(otpRequestDto);
        String otpDecode = OtpEncode.base64Decode(otpGenerated);

        if (otpRequestDto.getOption() == 0) {
            logger.info("Sending otp to sms...");
            point = sendToSms(otpRequestDto.getPhoneNumber(), otpDecode);
        }

        if (otpRequestDto.getOption() == 1) {
            logger.info("Sending otp to email...");
            point = sendToEmail(otpRequestDto.getEmail(), otpDecode);
        }

        String response = (point) ? StringConstants.OTP_OK : StringConstants.OTP_FAIL;
        logger.info(response);
        return new MessageResponse(response);
    }

    public boolean sendToSms(String phoneNumber, String otpGenerated) {
        try {
            String textSms = StringConstants.DESCRIPTION_SMS + otpGenerated;
            logger.info("Ok");
            return twilioConfig.send(textSms, phoneNumber);
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean sendToEmail(String toUser, String otpGenerated) {
        try {
            emailService.sendEmail(toUser, StringConstants.SUBJECT_OTP, otpGenerated);
            logger.info("Ok");
            return true;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
