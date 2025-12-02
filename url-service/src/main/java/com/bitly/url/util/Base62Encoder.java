package com.bitly.url.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {
    
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public String encode(Long num) {
        if (num == 0) {
            return String.valueOf(BASE62_CHARS.charAt(0));
        }

        StringBuilder result = new StringBuilder();
        while (num > 0) {
            result.append(BASE62_CHARS.charAt((int) (num % BASE)));
            num /= BASE;
        }

        return result.reverse().toString();
    }

    public Long decode(String str) {
        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            result = result * BASE + BASE62_CHARS.indexOf(str.charAt(i));
        }
        return result;
    }
}