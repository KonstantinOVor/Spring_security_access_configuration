package com.example.Spring_security_access_configuration.security.jwt.util;

import com.example.Spring_security_access_configuration.security.AppUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.*;


@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.tokenExpirationTime}")
    private Duration tokenExpiration;


    public String generateJwtToken(AppUserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        Date now = new Date();
        Date expirationDate = getExp(now);
        Key key = hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();

    }

    private Date getExp(Date now) {
        long l = tokenExpiration.toMillis();
        return new Date(now.getTime() + l);
    }
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }
    private Claims parseToken(String token) {

        Key key = hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateTokenWithoutUserDetails(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims != null && !isTokenExpired(claims)) {
                return true;
            }
        } catch (SignatureException e) {
            log.error("Неверная подпись JWT: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Невалидный JWT: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error("JWT устарел: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("JWT не поддерживается: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT пустой: {}", e.getMessage());
            return false;
        }
        return false;
    }

    private boolean isTokenExpired(Claims claims) {
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }
}
