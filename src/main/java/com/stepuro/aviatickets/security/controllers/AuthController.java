package com.stepuro.aviatickets.security.controllers;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.UserDto;
import com.stepuro.aviatickets.api.dto.UserMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.User;
import com.stepuro.aviatickets.security.models.JwtRefreshRequest;
import com.stepuro.aviatickets.security.models.JwtRequest;
import com.stepuro.aviatickets.security.models.JwtResponse;
import com.stepuro.aviatickets.security.services.AuthService;
import com.stepuro.aviatickets.security.utils.CookieUtil;
import com.stepuro.aviatickets.security.utils.JwtProvider;
import com.stepuro.aviatickets.services.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

@RestController
@RequestMapping("/api/v1/auth")
@Getter
@Setter
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CookieUtil cookieUtil;

    @PostMapping("login")
    public void login(@Valid @RequestBody JwtRequest authRequest, HttpServletResponse response) throws AuthException, ResourceNotFoundException {
        JwtResponse token = authService.login(authRequest);


        Cookie accessCookie = cookieUtil.createAccess(token.getAccessToken());

        Cookie refreshCookie = cookieUtil.createRefresh(token.getRefreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

    }

    @PostMapping("token")
    public void getNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException {

        String refreshToken = jwtProvider.getRefreshTokenFromCookie(request);

        final JwtResponse token = authService.getAccessToken(refreshToken);

        Cookie accessCookie = cookieUtil.createAccess(token.getAccessToken());

        response.addCookie(accessCookie);
    }

    @PostMapping("refresh")
    public void getNewRefreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthException, ResourceNotFoundException {
        String refreshToken = jwtProvider.getRefreshTokenFromCookie(request);

        final JwtResponse token = authService.refresh(refreshToken);

        Cookie accessCookie = cookieUtil.createAccess(token.getAccessToken());

        Cookie refreshCookie = cookieUtil.createRefresh(token.getRefreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

    }

    @PostMapping("register")
    public void register(@RequestBody @Valid UserDto userDto, HttpServletResponse response) throws AuthException{
        JwtResponse jwtResponse = authService.register(userDto);

        Cookie accessCookie = cookieUtil.createAccess(jwtResponse.getAccessToken());

        Cookie refreshCookie = cookieUtil.createRefresh(jwtResponse.getRefreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    @PostMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws AuthException, ResourceNotFoundException {
        response.addCookie(cookieUtil.removeAccess());
        response.addCookie(cookieUtil.removeRefresh());

    }

}
