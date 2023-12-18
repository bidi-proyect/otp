package com.bidi.otp.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface EmailService {

    void sendEmail(String toUser, String subject, String message);
    void sendEmailWithFile(String toUser, String subject, String message, File file);
}
