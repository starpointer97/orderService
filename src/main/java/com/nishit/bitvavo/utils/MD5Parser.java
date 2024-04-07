package com.nishit.bitvavo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Parser {

    public static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(input.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("MD5 algorithm not available");
            return null;
        }
    }
}
