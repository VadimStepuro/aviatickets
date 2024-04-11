package com.stepuro.aviatickets.security.services;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.UserDto;
import com.stepuro.aviatickets.api.mapper.UserMapper;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.User;
import com.stepuro.aviatickets.security.models.JwtRequest;
import com.stepuro.aviatickets.security.models.JwtResponse;
import com.stepuro.aviatickets.security.utils.JwtProvider;
import com.stepuro.aviatickets.services.implementation.RoleServiceImpl;
import com.stepuro.aviatickets.services.implementation.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserServiceImpl userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RoleServiceImpl roleService;


    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException, ResourceNotFoundException{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getLogin(),
                authRequest.getPassword()
        ));
        final User user = UserMapper.INSTANCE.userDtoToUser(userService.getByLogin(authRequest.getLogin()));
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getLogin(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);

    }
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws ResourceNotFoundException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = UserMapper.INSTANCE.userDtoToUser(userService.getByLogin(login));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException, ResourceNotFoundException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user =  UserMapper.INSTANCE.userDtoToUser(userService.getByLogin(login));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public Authentication getAuthInfo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Loggable
    public JwtResponse register(UserDto userDto) throws AuthException{
        JwtRequest request = new JwtRequest();
        request.setLogin(userDto.getLogin());
        request.setPassword(userDto.getPassword());
        userDto.setRoles(Collections.singletonList(roleService.findByName("ROLE_USER")));
        userService.create(userDto);


        return login(request);

    }
}
