package com.bill.springbootmall.util;

import com.bill.springbootmall.model.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;

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
        claims.setIssuer(ISS); // 設置發行者 (ISS)
        claims.put("userId", myUserDetails.getUserId()); // 將用戶 ID 加入 claims
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

    public static Integer getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        // 提取 id，這裡的 id 是之前生成 token 時加入的 Claim
        return claims.get("userId", Integer.class);  // 確保 id 是 Integer 類型
    }

}
