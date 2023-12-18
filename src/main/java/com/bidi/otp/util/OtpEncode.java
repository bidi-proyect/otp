package com.bidi.otp.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OtpEncode {
    public static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String base64Decode (String input){
        return new String(Base64.getDecoder().decode(input.getBytes()), StandardCharsets.UTF_8);
    }

}
