package com.example.Spring_security_access_configuration.web.controller;

import com.example.Spring_security_access_configuration.exception.AlreadyExistsException;
import com.example.Spring_security_access_configuration.repository.UserRepository;
import com.example.Spring_security_access_configuration.security.jwt.service.SecurityService;
import com.example.Spring_security_access_configuration.web.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest createUserRequest) {
        if(userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExistsException("Имя пользователя уже занято!");
        }
        if(userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new AlreadyExistsException("Электронная почта уже занята!");
        }
        securityService.register(createUserRequest);
        return ResponseEntity.ok(new SimpleResponse("Пользователь успешно зарегистрирован!"));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(securityService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        securityService.logout();
        return ResponseEntity.ok(new SimpleResponse("Вы вышли из системы"));
    }
}
