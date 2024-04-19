package com.benchmark.education.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    String secretKey = "kbjEPiBMebznn5Ipa6+XSM+NuFQvPAvpeIErYXojxQCfoA0WzQ53Kuo6y8VmZCSncMPnnFWvH8yvXqny/grPspp/crMRh4E5Bg88ZT5mSi0ovEE+fMsH3wmEtz4yyimESifjM6x50PN84H/AOoZbqQ6LK9dp5VUmXvRJXp+nPVqAp135ykZDH+vLFkfUm4CXEW33Lrbh8lD";


    private int otpTokenExpirationMS = 600000;
    private int accessTokenExpirationMS = 5000000;

    private int refreshTokenExpirationMS = 200000000;


    public String getOtpToken(String otp,  String email){
        String principal = otp+"_"+email;
        return Jwts.builder()
                .setSubject(principal)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + otpTokenExpirationMS))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getAccessToken(String username, String claimKey, Object claim){
        return Jwts.builder()
                .setSubject(username)
                .claim(claimKey, claim)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + accessTokenExpirationMS))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getAccessToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + accessTokenExpirationMS))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getRefreshToken(String username, String claimKey, Object claim){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + refreshTokenExpirationMS))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Object getClaimFromJWTToken(String token, String claimKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody().get(claimKey);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}