package com.bidi.otp.service.impl;

import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.dto.OtpResponseDto;
import com.bidi.otp.entity.OtpEntity;
import com.bidi.otp.exception.ApiException;
import com.bidi.otp.repository.OtpRepository;
import com.bidi.otp.service.OtpService;
import com.bidi.otp.util.MessageResponse;
import com.bidi.otp.util.OtpEncode;
import com.bidi.otp.util.StringResponse;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpImpl implements OtpService {
    @Value("${bidi.maximumRetries}")
    private Integer maximuTries;
    @Autowired
    private OtpRepository otpRepository;
    private static final Logger logger = LoggerFactory.getLogger(OtpImpl.class);
    private static final Random RANDOM = new Random();
    private static final ModelMapper MAPPER = new ModelMapper();
    private static long range = 9999999L;

    @Override
    public String generatedOtp(String userId, String phoneNumber) throws ApiException {
        logger.info("entro al servicio para generar un nuevo OTP");
        try {
            String otpGenerated;
            long randomNumber = (long) (Math.floor(RANDOM.nextDouble() * range) % 900000) + 100000;
            otpGenerated = String.valueOf(randomNumber);
            System.out.println(otpGenerated);

            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setOtpCode(OtpEncode.base64Encode(otpGenerated));
            LocalDateTime dateTime = LocalDateTime.now();
            otpEntity.setGenerationTime(dateTime);
            otpEntity.setPhoneNumber(OtpEncode.base64Encode(phoneNumber));
            otpEntity.setState("Generated");
            otpEntity.setUserId(userId);
            otpRepository.saveAndFlush(otpEntity);
            logger.info("OTP generado correctamete");
            return otpGenerated;
        } catch (Exception e) {
            throw new ApiException("error al generar OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public boolean sendOtpSms(String phoneNumber, String userId) throws ApiException {
        Twilio.init("AC01621c2e09e50990f736d5c3a4148a61", "9d13c22f833d243dd7c826ad44a8e890");
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+57" + phoneNumber),
                        new com.twilio.type.PhoneNumber("+15138135415"),
                        "\nbidi: \nDigita este codigo para confirmar tu cuenta: " + generatedOtp(userId, phoneNumber))
                .create();

        logger.debug("SID: {}", message.getSid());
        logger.info("Otp enviado por sms");
        return true;
    }

    @Override
    public boolean sendOtpEmail(String email, String userId) {
        String sender = email;
        String receiver;
        return true;
    }

    @Override
    public MessageResponse sendOtp(OtpRequestDto otpRequestDto, String userId) throws ApiException {
        boolean point = false;
        MessageResponse response;
        if (otpRequestDto.getOption() == 0) {
            point = sendOtpSms(otpRequestDto.getPhoneNumber(), userId);
        }
        if (otpRequestDto.getOption() == 1) {
            point = sendOtpEmail(otpRequestDto.getEmail(), userId);
        }
        return (point) ? new MessageResponse(StringResponse.OTP_OK) : new MessageResponse(StringResponse.OTP_FAIL);
    }

    @Override
    public boolean validateOtp(String otpCode) {
        OtpEntity otpEntity = otpRepository.findByOtpCode(OtpEncode.base64Encode(otpCode));
        if (otpEntity != null){
            otpEntity.setState("Validate");
            otpRepository.saveAndFlush(otpEntity);
            return true;
        }
        otpEntity.setState("Not valid");
        otpRepository.saveAndFlush(otpEntity);
        return false;
    }

}
