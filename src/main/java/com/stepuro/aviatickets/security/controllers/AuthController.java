package com.stepuro.aviatickets.security.controllers;

import com.stepuro.aviatickets.api.dto.UserDto;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.security.models.JwtRequest;
import com.stepuro.aviatickets.security.models.JwtResponse;
import com.stepuro.aviatickets.security.services.AuthService;
import com.stepuro.aviatickets.security.utils.CookieUtil;
import com.stepuro.aviatickets.security.utils.JwtProvider;
import com.stepuro.aviatickets.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Login into system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful and token added to cookies",
                    content = { @Content(mediaType = "application/json")})})
    @PostMapping("login")
    public void login(@Valid @RequestBody JwtRequest authRequest, HttpServletResponse response) throws AuthException, ResourceNotFoundException {
        JwtResponse token = authService.login(authRequest);


        Cookie accessCookie = cookieUtil.createAccess(token.getAccessToken());

        Cookie refreshCookie = cookieUtil.createRefresh(token.getRefreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

    }

    @Operation(summary = "Get new access token with refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed",
                    content = { @Content(mediaType = "application/json")})})
    @PostMapping("token")
    public void getNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException {

        String refreshToken = jwtProvider.getRefreshTokenFromCookie(request);

        final JwtResponse token = authService.getAccessToken(refreshToken);

        Cookie accessCookie = cookieUtil.createAccess(token.getAccessToken());

        response.addCookie(accessCookie);
    }

    @Operation(summary = "Get new access and refresh token with refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access and refresh token refreshed",
                    content = { @Content(mediaType = "application/json")})})
    @PostMapping("refresh")
    public void getNewRefreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthException, ResourceNotFoundException {
        String refreshToken = jwtProvider.getRefreshTokenFromCookie(request);

        final JwtResponse token = authService.refresh(refreshToken);

        Cookie accessCookie = cookieUtil.createAccess(token.getAccessToken());

        Cookie refreshCookie = cookieUtil.createRefresh(token.getRefreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

    }

    @Operation(summary = "Register into system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register successful and token added to cookies",
                    content = { @Content(mediaType = "application/json")})})
    @PostMapping("register")
    public void register(@RequestBody @Valid UserDto userDto, HttpServletResponse response) throws AuthException{
        JwtResponse jwtResponse = authService.register(userDto);

        Cookie accessCookie = cookieUtil.createAccess(jwtResponse.getAccessToken());

        Cookie refreshCookie = cookieUtil.createRefresh(jwtResponse.getRefreshToken());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    @Operation(summary = "Logout from system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful and token added to cookies",
                    content = { @Content(mediaType = "application/json")})})
    @PostMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws AuthException, ResourceNotFoundException {
        response.addCookie(cookieUtil.removeAccess());
        response.addCookie(cookieUtil.removeRefresh());

    }

}
