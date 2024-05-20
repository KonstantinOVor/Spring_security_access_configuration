package com.example.Spring_security_access_configuration.security.config;

import com.example.Spring_security_access_configuration.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableRedisRepositories(
        keyspaceConfiguration = RedisConfiguration.RefreshTokenKeyspaceConfiguration.class,
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfiguration {

    @Value("${jwt.refreshTokenExpirationTime}")
    private Duration refreshTokenExpiration;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisProperties properties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setHostName(properties.getHost());
        configuration.setPort(properties.getPort());

        return new JedisConnectionFactory(configuration);
    }

    public class RefreshTokenKeyspaceConfiguration extends KeyspaceConfiguration {
        private static final String REFRESH_TOKEN_KEYSPACE   = "refresh_tokens";

        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings settings = new KeyspaceSettings(RefreshToken.class, REFRESH_TOKEN_KEYSPACE);

            settings.setTimeToLive(refreshTokenExpiration.getSeconds());

            return Collections.singleton(settings);
        }
    }
}
