package com.hibob.anylink.user.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

@Configuration
public class SecurityUtil {

    @Value("${custom.fixed-key}")
    private String FIXED_KEY;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String decryptPassword(String nonce, String ivHex, String ciphertextHex, String authTagHex)
            throws Exception {

        // 1. 生成动态密钥
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(FIXED_KEY.getBytes(), "HmacSHA256"));
        byte[] dynamicKey = Arrays.copyOfRange(hmac.doFinal(nonce.getBytes()), 0, 32);

        // 2. 准备参数
        byte[] iv = hexToBytes(ivHex);
        byte[] ciphertext = hexToBytes(ciphertextHex);
        byte[] authTag = hexToBytes(authTagHex);

        // 3. 组合加密数据
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(ciphertext);
        bos.write(authTag);
        byte[] encryptedData = bos.toByteArray();

        // 4. 配置解密器
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(dynamicKey, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        // 5. 执行解密
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
        return new String(cipher.doFinal(encryptedData));
    }

    // 十六进制字符串转字节数组
    private byte[] hexToBytes(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int high = Character.digit(hex.charAt(i*2), 16);
            int low = Character.digit(hex.charAt(i*2+1), 16);

            if (high == -1 || low == -1) {
                throw new IllegalArgumentException("Invalid hex character");
            }

            bytes[i] = (byte) ((high << 4) | low);
        }
        return bytes;
    }
}
