package com.huah.io.file;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FileCryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "ThisIsASecretKey"; // 16字符秘钥

    // 加密文件
    public static void encryptFile(String inputPath, String outputPath) throws Exception {
        byte[] fileContent = Files.readAllBytes(Paths.get(inputPath));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(fileContent);
        Files.write(Paths.get(outputPath), Base64.getEncoder().encode(encrypted));
    }

    // 解密文件
    public static void decryptFile(String inputPath, String outputPath) throws Exception {
        byte[] fileContent = Base64.getDecoder().decode(Files.readAllBytes(Paths.get(inputPath)));
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(fileContent);
        Files.write(Paths.get(outputPath), decrypted);
    }

    public static void main(String[] args) throws Exception {
        // 示例文件: 在D盘创建test.txt (内容随意)
        encryptFile("D:/test.txt", "D:/encrypted.txt");
        decryptFile("D:/encrypted.txt", "D:/decrypted.txt");
        System.out.println("文件加解密完成!");
    }
}