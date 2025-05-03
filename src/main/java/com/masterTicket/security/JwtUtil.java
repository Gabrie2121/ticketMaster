package com.masterTicket.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {
    
    //private final String secret;
    private final long expirationMillis;
    private final Algorithm algorithm;


    public JwtUtil(@Value("${JWT_SECRET}") String secret, @Value("${JWT_EXPIRATION}") long expirationMillis) {
        this.expirationMillis = expirationMillis;
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(String username, String role){
        Date now = new Date();
        Date expiry = new Date(now.getTime()+expirationMillis);

        return JWT.create()
        .withSubject(username)
        .withClaim("role", role)
        .withIssuedAt(now)
        .withExpiresAt(expiry)
        .sign(algorithm);
    }

    public boolean isTokenValid(String token){
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private DecodedJWT decode(String token){
        return JWT.require(algorithm).build().verify(token);
    }

    private String extractUsername(String token){
        return decode(token).getSubject();
    }

    public String extractRole(String token){
        return decode(token).getClaim("ROLE").asString();
    }
}
