package org.imageghost.Test;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import javax.crypto.Cipher;

public class RsaUtil {
    private KeyPairGenerator generator;
    private KeyFactory keyFactory;
    private KeyPair keypair;
    private Cipher cipher;

    // 1024비트 RSA 키쌍을 생성
    public RsaUtil() {
        try{
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            keyFactory = KeyFactory.getInstance("RSA");
            cipher = Cipher.getInstance("RSA");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> createRSA() {
        HashMap<String, Object> rsa = new HashMap<String, Object>();
        try{
            keypair = generator.generateKeyPair();
            PublicKey publicKey = keypair.getPublic();
            PrivateKey privateKey = keypair.getPrivate();

            RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String modulus = publicSpec.getModulus().toString(16);
            String exponent = publicSpec.getPublicExponent().toString(16);
            rsa.put("publicKey", publicKey);
            rsa.put("privateKey", privateKey);
            rsa.put("modulus", modulus);
            rsa.put("exponent", exponent);

        }catch (Exception e){
            e.printStackTrace();
        }

        return rsa;
    }

    // Key로 RSA 복호화를 수행
    public String getDecryptText(PrivateKey privateKey, String ecryptText) throws Exception {
        cipher.init(cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(hexToByteArray(ecryptText));

        return new String(decryptedBytes, "UTF-8");
    }

    // Key로 RSA 암호화를 수행
    public String setEncryptText(PrivateKey publicKey, String encryptText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(encryptText.getBytes());

        return new String(encryptedBytes, "UTF-8");
    }

    private byte[] hexToByteArray(String hex){
        if(hex == null || hex.length() % 2 != 0){
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < hex.length(); i += 2){
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }

        return bytes;
    }
}