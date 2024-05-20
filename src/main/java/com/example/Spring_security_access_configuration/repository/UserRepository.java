package com.example.Spring_security_access_configuration.repository;

import com.example.Spring_security_access_configuration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
