package com.example.Spring_security_access_configuration.exception;

import java.text.MessageFormat;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String token, String message) {
        super(MessageFormat.format(" Ошибка при проверке Refresh-токен: {0} : {1}", token, message));
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}
