package com.example.Spring_security_access_configuration.security.jwt.service;

import com.example.Spring_security_access_configuration.entity.RefreshToken;
import com.example.Spring_security_access_configuration.entity.User;
import com.example.Spring_security_access_configuration.exception.RefreshTokenException;
import com.example.Spring_security_access_configuration.repository.UserRepository;
import com.example.Spring_security_access_configuration.security.AppUserDetails;
import com.example.Spring_security_access_configuration.security.jwt.util.JwtUtil;
import com.example.Spring_security_access_configuration.service.RefreshTokenService;
import com.example.Spring_security_access_configuration.web.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

   public AuthResponse authenticate(LoginRequest loginRequest) {

       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               loginRequest.getUsername(),
               loginRequest.getPassword()
       ));

       SecurityContextHolder.getContext().setAuthentication(authentication);

       AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

       List<String> roles = userDetails.getAuthorities().stream()
               .map(GrantedAuthority::getAuthority)
               .toList();

       RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

       return AuthResponse.builder()
               .id(userDetails.getId())
               .token(jwtUtil.generateJwtToken(userDetails))
               .refreshToken(refreshToken.getToken())
               .username(userDetails.getUsername())
               .email(userDetails.getEmail())
               .roles(roles)
               .build();
   }

   public void register(CreateUserRequest createUserRequest) {
       User user = User.builder()
               .username(createUserRequest.getUsername())
               .password(passwordEncoder.encode(createUserRequest.getPassword()))
               .email(createUserRequest.getEmail())
               .build();

       user.setRole(createUserRequest.getRoles());

       userRepository.save(user);
   }

   public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
       String token = refreshTokenRequest.getRefreshToken();

       return refreshTokenService.findByRefreshToken(token)
               .map(refreshTokenService::checkRefreshToken)
               .map(RefreshToken::getUserId)
               .map(user -> {
                   User tokenUser = userRepository.findById(user).orElseThrow(() ->
                           new RefreshTokenException("Пользователь не найден", token));
                   String jwt = jwtUtil.generateTokenFromUsername(tokenUser.getUsername());

                   return new RefreshTokenResponse(jwt, refreshTokenService.createRefreshToken(user).getToken());
               }).orElseThrow(() -> new RefreshTokenException(token,"Refresh-токен не найден"));
   }

   public void logout() {
       var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if(principal instanceof AppUserDetails userDetails) {
           Long userId = userDetails.getId();
           refreshTokenService.deleteByUserId(userId);
       }
   }
}
