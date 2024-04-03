package com.example.capt_2.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretkey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    //Generate acces
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Validate token

    public boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        }catch(Exception e){
            log.error("Token invalid: " .concat(e.getMessage()));
            return false;
        }
    }

    //Get username from token
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    //Get only 1 claim
    public <T> T getClaim(String token, Function<Claims,T> claimsFuntion){
        Claims claims = extractAllClaims(token);
        return claimsFuntion.apply(claims);
    }

    //Get all the claims of token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSigningKey() {
       byte[] keyBytes = Decoders.BASE64.decode(secretkey);
       return Keys.hmacShaKeyFor(keyBytes);
    }
}
