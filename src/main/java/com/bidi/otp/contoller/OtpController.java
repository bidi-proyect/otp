package com.bidi.otp.contoller;

import com.bidi.otp.dto.OtpRequestDto;
import com.bidi.otp.dto.OtpResponseDto;
import com.bidi.otp.exception.ApiException;
import com.bidi.otp.service.OtpService;
import com.bidi.otp.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/otp")
@CrossOrigin(origins = "*")
public class OtpController {
    @Autowired
     private  OtpService otpService;

    @PostMapping("/generate/{userId}")
    public String generateCode (@PathVariable String userId, @RequestBody OtpRequestDto otpRequestDto)throws ApiException {
        return  otpService.generatedOtp(userId,otpRequestDto.getPhoneNumber());
    }

    @PostMapping("/send/{userId}")
    public MessageResponse sendOtp (@PathVariable String userId, @RequestBody OtpRequestDto otpRequestDto) throws ApiException {
        return otpService.sendOtp(otpRequestDto,userId);
    }

    @PostMapping("/validate/{userId}/{otpCode}")
    public boolean validateOtp(@PathVariable String otpCode){
        return otpService.validateOtp(otpCode);
    }
}
