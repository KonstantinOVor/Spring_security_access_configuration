package com.example.Spring_security_access_configuration.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Long id;
    private String token;
    private String refreshToken;
    private String username;
    private String email;
    private List<String> roles;
}
