package com.example.Spring_security_access_configuration.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app")
@RequiredArgsConstructor
public class AppController {

    @GetMapping("/all")
    public String allAccess() {
        return "Публичный контент";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Административный контент";
    }
    @GetMapping("/secured")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String securedAccess() {
        return "Защищенный контент";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public String userAccess() {
        return "Приватный контент";
    }
}
