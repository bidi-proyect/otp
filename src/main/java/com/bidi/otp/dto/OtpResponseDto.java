package com.bidi.otp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpResponseDto {
    private long idOtp;
    private String userId;
    private String otpCode;
    private LocalDateTime generationTime;
    private String phoneNumber;
    private String email;
    private String status;
}
