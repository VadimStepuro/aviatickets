package com.stepuro.aviatickets.security.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${stepuro.app.accessTokenCookieName}")
    private String jwtAccessTokenName;
    @Value("${stepuro.app.refreshTokenCookieName}")
    private String jwtRefreshTokenName;

    @Value("${stepuro.app.jwtExpirationMs}")
    private int accessTokenDuration;
    @Value("${stepuro.app.jwtRefreshExpirationMs}")
    private int refreshtokenDuration;

    public Cookie createAccess(String jwtAccessToken){
        Cookie accessCookie = new Cookie(jwtAccessTokenName, jwtAccessToken);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(accessTokenDuration/1000);
        accessCookie.setSecure(false);
        return accessCookie;
    }

    public Cookie createRefresh(String jwtRefreshToken){
        Cookie refreshCookie = new Cookie(jwtRefreshTokenName, jwtRefreshToken);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(refreshtokenDuration/1000);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        return refreshCookie;
    }

    public Cookie removeAccess(){
        Cookie accessCookie = new Cookie(jwtAccessTokenName, null);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(0);
        accessCookie.setSecure(false);
        return accessCookie;
    }

    public Cookie removeRefresh(){
        Cookie refreshCookie = new Cookie(jwtRefreshTokenName, null);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        return refreshCookie;
    }

}
