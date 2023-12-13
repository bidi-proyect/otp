package com.bidi.otp;

import com.bidi.otp.service.OtpService;
import com.bidi.otp.service.impl.OtpImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OtpApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtpApplication.class, args);
		OtpImpl otpService = new OtpImpl();
		//int randomNumber= (int)(Math.random()*9000)+100000;
		//System.out.println("Este es el numero del otp: impl "+ otpService.generatedOtp("3213613616","lxalarcon@gmail.com"));
		}

	}


