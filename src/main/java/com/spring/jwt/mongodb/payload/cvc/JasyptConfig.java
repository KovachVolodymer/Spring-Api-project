package com.spring.jwt.mongodb.payload.cvc;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {
    @Value("${kovach.app.jwtSecret}")
    private String jwtSecret;

    @Bean("strongEncryptor")
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jwtSecret); // Встановіть пароль для шифрування/розшифрування
        return encryptor;
    }
}

