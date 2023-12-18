package com.bidi.otp.service.impl;

import com.bidi.otp.entity.OtpEntity;
import com.bidi.otp.exception.ApiException;
import com.bidi.otp.repository.OtpRepository;
import com.bidi.otp.service.ValidateOtpService;
import com.bidi.otp.util.OtpEncode;
import com.bidi.otp.util.StringConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ValidateOtpImpl implements ValidateOtpService {

    private final OtpRepository otpRepository;
    private static final Logger logger = LoggerFactory.getLogger(ValidateOtpImpl.class);

    @Override
    public void validateOtp(String otpCode) {
        OtpEntity otpEntity = otpRepository.findByOtpCode(OtpEncode.base64Encode(otpCode));

        if (otpEntity == null) {
            throw new ApiException("Otp not found.", HttpStatus.CONFLICT);
        }

        if (otpEntity.getState().equals(StringConstants.EXPIRED)) {
            throw new ApiException("Otp Expire.", HttpStatus.CONFLICT);
        }

        if (otpEntity.getState().equals(StringConstants.ACTIVE)) {
            otpEntity.setState(StringConstants.VALID);
            otpRepository.saveAndFlush(otpEntity);
        }

        logger.info("Otp is Validated.");
    }
}
