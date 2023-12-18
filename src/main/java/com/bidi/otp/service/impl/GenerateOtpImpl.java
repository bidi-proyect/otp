package com.bidi.otp.service.impl;

import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.dto.OtpResponseDto;
import com.bidi.otp.entity.OtpEntity;
import com.bidi.otp.exception.ApiException;
import com.bidi.otp.repository.OtpRepository;
import com.bidi.otp.service.GenerateOtpService;
import com.bidi.otp.util.OtpEncode;
import com.bidi.otp.util.StringConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GenerateOtpImpl implements GenerateOtpService {
    private final Random random = new Random();
    private final OtpRepository otpRepository;
    private static final Logger logger = LoggerFactory.getLogger(GenerateOtpImpl.class);

    @Override
    public OtpResponseDto generateOtpWithResponse(OtpRequestDto otpRequestDto) {
        logger.info("Generating new Otp code...");
        try {
            String otpGenerated = deleteHyphen(String.valueOf(((random.nextLong() * 9999999) % 900000) + 100000));
            String otpEncoded = OtpEncode.base64Encode(otpGenerated);

            LocalDateTime dateTime = LocalDateTime.now();
            startCountdown(otpEncoded);

            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setOtpCode(otpEncoded);
            otpEntity.setGenerationTime(dateTime);
            otpEntity.setPhoneNumber(whatDataSave(otpRequestDto.getPhoneNumber()));
            otpEntity.setEmail(whatDataSave(otpRequestDto.getEmail()));
            otpEntity.setState(StringConstants.ACTIVE);
            otpEntity.setUserId(otpRequestDto.getUserId());

            this.otpRepository.saveAndFlush(otpEntity);
            logger.info("Otp generated successfully.");

            return entityToResponse(otpEntity);
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            throw new ApiException("Error: ".concat(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String generateOtp(OtpRequestDto otpRequestDto) {
        logger.info("Generating new Otp code...");
        try {
            String otpGenerated = String.valueOf(((random.nextLong() * 9999999) % 900000) + 100000);
            String otpEncoded = OtpEncode.base64Encode(deleteHyphen(otpGenerated));

            LocalDateTime dateTime = LocalDateTime.now();
            startCountdown(otpEncoded);

            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setOtpCode(otpEncoded);
            otpEntity.setGenerationTime(dateTime);
            otpEntity.setPhoneNumber(whatDataSave(otpRequestDto.getPhoneNumber()));
            otpEntity.setEmail(whatDataSave(otpRequestDto.getEmail()));
            otpEntity.setState(StringConstants.ACTIVE);
            otpEntity.setUserId(otpRequestDto.getUserId());

            otpRepository.saveAndFlush(otpEntity);
            logger.info("Otp generated successfully.");

            return otpEncoded;
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            throw new ApiException("Error: ".concat(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public OtpResponseDto entityToResponse(OtpEntity otpEntity) {
        OtpResponseDto otpResponseDto = new OtpResponseDto();
        otpResponseDto.setIdOtp(otpEntity.getIdOtp());
        otpResponseDto.setUserId(otpEntity.getUserId());
        otpResponseDto.setOtpCode(OtpEncode.base64Decode(otpEntity.getOtpCode()));
        otpResponseDto.setGenerationTime(otpEntity.getGenerationTime());
        otpResponseDto.setPhoneNumber(otpEntity.getPhoneNumber());
        otpResponseDto.setEmail(otpEntity.getEmail());
        otpResponseDto.setStatus(otpEntity.getState());
        return otpResponseDto;
    }

    public String whatDataSave(String input) {
        if (input.matches(StringConstants.NUMERIC_REGEX)) {
            return OtpEncode.base64Encode(input);
        } else if (input.matches(StringConstants.EMAIL_REGEX)) {
            return OtpEncode.base64Encode(input);
        } else {
            return "NA";
        }
    }

    public String deleteHyphen(String input) {
        if (input.charAt(0) == '-')
            return input.substring(1);
        return input;
    }

    private void startCountdown(String otpGenerated) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        logger.info("Otp Schedule Started...");
        scheduler.schedule(() -> {
            OtpEntity entity = otpRepository.findByOtpCode(otpGenerated);
            if (entity.getState().equals(StringConstants.ACTIVE)) {
                logger.info("Otp expired");
                entity.setState(StringConstants.EXPIRED);
                otpRepository.saveAndFlush(entity);
            }
            scheduler.shutdown();
            logger.info("Otp Schedule Finalized.");
        }, 1, TimeUnit.MINUTES);
    }

}
