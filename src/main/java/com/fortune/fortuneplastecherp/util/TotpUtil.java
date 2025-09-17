package com.fortune.fortuneplastecherp.util;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;

public class TotpUtil {
    public static String generateSecret() {
        return Base32.random();
    }

    public static boolean verifyCode(String secret, String code) {
        Totp totp = new Totp(secret);
        return totp.verify(code);
    }

    public static String getOtpAuthUrl(String user, String secret, String issuer) {
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, user, secret, issuer);
    }
}
