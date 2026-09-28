package com.stirwick.hotelmanagement.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 职责：生成token、解析token、校验token
 * JWT字符串格式：header.payload.signature
 * header：加密算法
 * payload：载荷，存放业务数据（base64可解码，禁止存密码）
 * signature：签名，密钥加密，防止篡改
 */
@Component
public class JwtUtil {

    // 从yml读取密钥
    @Value("${jwt.secret}")
    private String secret;

    // token过期时间，单位分钟
    @Value("${jwt.expire-minutes}")
    private Long expireMinutes;

    /**
     * 将yml字符串密钥转为jjwt需要的加密密钥对象SecretKey
     */
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT token字符串
     * @param userId 登录用户ID
     * @return token字符串
     */
    public String generateToken(Long userId){
        // payload载荷，存放我们的数据
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // 计算过期时间：当前时间 + 过期毫秒数
        long expireMs = expireMinutes * 60 * 1000;
        Date expireDate = new Date(System.currentTimeMillis() + expireMs);

        // jjwt构建JWT
        return Jwts.builder()
                .setClaims(claims)               // 设置载荷数据
                .setExpiration(expireDate)       // 设置过期时间
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // HS256签名加密
                .compact(); // 拼接输出最终token字符串
    }

    /**
     * 解析token，获取里面的userId
     * @param token JWT字符串
     * @return 用户id
     */
    public Long getUserIdByToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) // 使用相同密钥校验签名
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 校验token是否合法：是否过期、是否被篡改、格式是否正确
     * @param token token字符串
     * @return true合法；false失效
     */
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            // token过期
            return false;
        } catch (JwtException e){
            // 篡改、格式错误、密钥不对
            return false;
        }
    }
}
