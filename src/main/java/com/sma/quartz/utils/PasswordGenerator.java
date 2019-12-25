package com.sma.quartz.utils;

import com.nimbusds.jose.util.Base64;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final int LENGTH = 20;

    public static String generate() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[LENGTH];
        random.nextBytes(bytes);
        return Base64.encode(bytes).toString();
    }
}
