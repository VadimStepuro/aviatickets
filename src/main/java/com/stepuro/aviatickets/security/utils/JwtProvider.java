package com.stepuro.aviatickets.security.utils;

import com.stepuro.aviatickets.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    @Value("${stepuro.app.accessTokenCookieName}")
    private String jwtAccessTokenName;

    @Value("${stepuro.app.refreshTokenCookieName}")
    private String jwtRefreshTokenName;

    @Value("${stepuro.app.jwtExpirationMs}")
    private Long accessTokenDuration;
    @Value("${stepuro.app.jwtRefreshExpirationMs}")
    private Long refreshtokenDuration;


    public JwtProvider(
            @Value("${stepuro.app.secretKey.access}") String jwtAccessSecret,
            @Value("${stepuro.app.secretKey.refresh}") String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(@NonNull User user) {
        final Instant accessExpirationInstant = LocalDateTime.now().plus(accessTokenDuration, ChronoUnit.MILLIS).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .compact();
    }

    public String generateRefreshToken(@NonNull User user) {
        final Instant refreshExpirationInstant = LocalDateTime.now().plus(refreshtokenDuration, ChronoUnit.MILLIS).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException expEx){
            log.error("Expired jwt", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (jwtAccessTokenName.equals(cookie.getName())) {
                String accessToken = cookie.getValue();
                if (accessToken == null) return null;

                return accessToken;
            }
        }
        return null;
    }
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (jwtRefreshTokenName.equals(cookie.getName())) {
                String accessToken = cookie.getValue();
                if (accessToken == null) return null;

                return accessToken;
            }
        }
        return null;
    }

}
