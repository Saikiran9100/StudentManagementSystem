package com.example.MiniProject.Utils;

import com.example.MiniProject.config.EncryptionConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AESUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 16;

    private final EncryptionConfig encryptionConfig;

    public String encrypt(String data) {

        try {

            byte[] iv = new byte[IV_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);

            SecretKeySpec key = new SecretKeySpec(
                    encryptionConfig.getSecretKey().getBytes(),
                    "AES"
            );

            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            byte[] encrypted = cipher.doFinal(data.getBytes());

            byte[] encryptedWithIv = new byte[IV_LENGTH + encrypted.length];

            System.arraycopy(iv, 0, encryptedWithIv, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, encryptedWithIv, IV_LENGTH, encrypted.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedData) {

        try {

            byte[] decoded = Base64.getDecoder().decode(encryptedData);

            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[decoded.length - IV_LENGTH];

            System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);
            System.arraycopy(decoded, IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);

            SecretKeySpec key = new SecretKeySpec(
                    encryptionConfig.getSecretKey().getBytes(),
                    "AES"
            );

            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted);

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}