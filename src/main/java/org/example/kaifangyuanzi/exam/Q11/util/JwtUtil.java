package org.example.kaifangyuanzi.exam.Q11.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

@Component("q11JwtUtil")
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private long expire;

    public String generateToken(String username){
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("username",username);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder().claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expire))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return !claims.getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }

    }

    public String getUsername(String token){
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username",String.class);
    }


}
