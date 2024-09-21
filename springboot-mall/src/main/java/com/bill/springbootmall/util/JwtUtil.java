package com.bill.springbootmall.util;

import com.bill.springbootmall.model.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final String ISS = "Hogwarts";
    private static final String SECRET = "AlohomoraIsASpellUsedToOpenDoors";
    private static final int EXPIRE_TIME = 5;

    public static String createToken(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.MINUTE, EXPIRE_TIME);

        Claims claims = Jwts.claims();
        claims.setSubject(myUserDetails.getEmail()); // 將 email 作為主題
        claims.setExpiration(exp.getTime()); // 設置過期時間

        // 將角色權限轉換為字符串列表並存入 JWT
        List<String> roles = myUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // 獲取權限的字符串表示（如 "ROLE_ADMIN"）
                .collect(Collectors.toList());
        claims.put("roles", roles); // 將轉換後的角色列表加入 claims

        claims.setIssuer(ISS); // 設置發行者 (ISS)
        Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes()); // SECRET 是你的簽名密鑰

        return Jwts.builder()
                .setClaims(claims) // 設置 Claims
                .signWith(secretKey) // 設置簽名密鑰
                .compact(); // 壓縮並生成最終的 JWT token 字符串
    }

    public static Claims parseToken(String token) {
        Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey).build();

        // 解析 JWT token，獲取 Claims
        return parser.parseClaimsJws(token).getBody();
    }

    public static String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        // 提取 email（作為 subject）
        return claims.getSubject();
    }

    // 從 Token 提取 userId
    public static Integer getUserIdFromToken() {
        // 從 SecurityContextHolder 獲取當前已認證的用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 確認身份信息存在並且是 MyUserDetails 類型
        if (authentication != null ) {
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            Integer userId = myUserDetails.getUserId();
            return userId;
        } else {
            return null;
        }
    }

    public static List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = parseToken(token);
        List<String> roles = claims.get("roles", List.class); // 提取 roles

        // 將角色字符串轉換為 GrantedAuthority 列表
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


}
