package com.bidi.otp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpRequestDto {
    @NotNull(message = "userId cannot be null.")
    private String userId;
    @NotNull(message = "Option cannot be null.")
    private int option;
    private String phoneNumber;
    private String email;
}
