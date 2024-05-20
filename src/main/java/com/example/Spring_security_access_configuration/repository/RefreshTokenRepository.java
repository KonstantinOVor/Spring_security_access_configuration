package com.example.Spring_security_access_configuration.repository;

import com.example.Spring_security_access_configuration.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(Long userId);
}
