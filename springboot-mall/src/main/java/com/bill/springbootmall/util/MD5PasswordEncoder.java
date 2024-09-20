package com.bill.springbootmall.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        // 使用 Spring 的 DigestUtils 進行 MD5 編碼
        return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 將輸入的明文密碼進行 MD5 編碼後，與已經編碼的密碼進行比較
        String encodedRawPassword = DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
        return encodedRawPassword.equals(encodedPassword);
    }
}
