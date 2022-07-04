package com.letmeclean.service.encryption;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SHA256EncryptionService implements PasswordEncoder {

    private static final String SHA_256 = "SHA-256";

    @Override
    public String encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            byte[] passBytes = password.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toString((digested[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("알고리즘을 찾지 못했습니다.");
            throw new RuntimeException();
        }
    }
}
