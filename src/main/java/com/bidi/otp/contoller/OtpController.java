package com.bidi.otp.contoller;

import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.dto.OtpResponseDto;
import com.bidi.otp.service.GenerateOtpService;
import com.bidi.otp.service.SendOtpService;
import com.bidi.otp.service.ValidateOtpService;
import com.bidi.otp.util.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OtpController {

    private final ValidateOtpService validateOtpService;
    private final GenerateOtpService generateOtpService;
    private final SendOtpService sendOtpService;

    @PostMapping("/generate/")
    @ResponseStatus(HttpStatus.OK)
    public OtpResponseDto generateCode(@RequestBody OtpRequestDto otpRequestDto) {
        return generateOtpService.generateOtpWithResponse(otpRequestDto);
    }

    @PostMapping("/send/")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse sendOtp(@RequestBody OtpRequestDto otpRequestDto) {
        return sendOtpService.sendOtp(otpRequestDto);
    }

    @PostMapping("/validate/{otpCode}")
    @ResponseStatus(HttpStatus.OK)
    public void validateOtp(@PathVariable String otpCode) {
        validateOtpService.validateOtp(otpCode);
    }
}
