package com.example.Spring_security_access_configuration.web.model;

import com.example.Spring_security_access_configuration.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String username;
    private String email;
    private Set<RoleType> roles;
    private String password;

}
