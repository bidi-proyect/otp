package com.bidi.otp.config;

import com.bidi.otp.exception.ApiException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class TwilioConfig {
    @Value("${sms.sender.sid}")
    private String smsSid;
    @Value("${sms.sender.pw}")
    private String smsPw;
    @Value("${sms.sender.number}")
    private String smsNumber;
    @Value("${sms.sender.prefix}")
    private String smsPrefix;

    private static final Logger logger = LoggerFactory.getLogger(TwilioConfig.class);


    public boolean send(String text, String phoneNumber) {
        try {
            Twilio.init(smsSid, smsPw);
            PhoneNumber to = new PhoneNumber(smsPrefix + phoneNumber);
            PhoneNumber from = new PhoneNumber(smsNumber);
            Message message = Message.creator(to, from, text).create();

            logger.debug("SID: {}", message.getSid());
            logger.info(text);
            return true;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
