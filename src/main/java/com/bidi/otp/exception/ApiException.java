package com.bidi.otp.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
public class ApiException extends Exception{
    private final String message;
    private final HttpStatus httpStatus;
}