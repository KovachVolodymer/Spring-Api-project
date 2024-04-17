package com.spring.jwt.mongodb.payload.cvc;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CVVEncryptionService {

    @Autowired
    private StringEncryptor strongEncryptor; // Інжектимо StringEncryptor

    public String encryptCVV(String cvv) {
        return strongEncryptor.encrypt(cvv);
    }

    public String decryptCVV(String encryptedCVV) {
        return strongEncryptor.decrypt(encryptedCVV);
    }
}

