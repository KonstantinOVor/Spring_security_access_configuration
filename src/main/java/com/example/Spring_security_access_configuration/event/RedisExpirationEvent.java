package com.example.Spring_security_access_configuration.event;

import com.example.Spring_security_access_configuration.entity.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisExpirationEvent {

    @EventListener
    public void handleRedisKeyExpirationEvent(RedisKeyExpiredEvent<RefreshToken> event) {

        RefreshToken refreshToken = (RefreshToken)event.getValue();
        if(refreshToken == null) {
            throw new RuntimeException("Refresh-токен не найден");
        }
        log.info("Refresh-токен {} c идентификатором {} истек", refreshToken.getToken(), refreshToken.getId());
    }
}
