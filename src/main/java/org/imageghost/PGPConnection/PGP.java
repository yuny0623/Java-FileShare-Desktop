package org.imageghost.PGPConnection;

import javax.crypto.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PGP {
    /*
        PGP Communication Implementation - by yuny0623
     */
    private String plainText;
    private String senderPublicKey;
    private String senderPrivateKey;
    private String receiverPublicKey;

    public PGP(String plainText, String senderPublicKey, String senderPrivateKey, String receiverPublicKey){
        this.plainText = plainText;
        this.senderPublicKey = senderPublicKey;
        this.senderPrivateKey = senderPrivateKey;
        this.receiverPublicKey = receiverPublicKey;
    }

    public void setPlainText(String plainText){

    }
    public void setSenderPublicKey(String senderPublicKey){

    }

    public void setSenderPrivateKey(String senderPrivateKey){

    }

    public void setReceiverPublicKey(String receiverPublicKey){

    }

    /*
        Alice:
           1. generate MAC
           2. encrypt MAC with Alice's private key
           3. add original message to result of 2
           4. generate new symmetric key
           5. encrypt 3 with the result of 4. the symmetric key
           6. put symmetric key in E.E (encrypt E.E with Bob's public key)
           7. add all result and send to Bob

        Bob:
           1.
           2.
           3.
           4.
           5.
           6.
           7.
     */

    /*
        1: MAC 생성
     */
    public String generateMAC(String plainText) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /*
        2. MAC 암호화
     */
    public String encryptMAC(String MAC){
        return encryptWithPrivateKey(MAC, this.senderPrivateKey);
    }

    /*
        private key로 전자서명에 사인
     */
    public String encryptWithPrivateKey(String plainText, String senderPrivateKey) {
        PrivateKey privateKey;
        String encryptedText = "";

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(senderPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            privateKey = keyFactory.generatePrivate(privateKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            // 평문 private key 로 암호화
            encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedText;
    }

    /*
        public key로 전자서명 풀기
     */
    public String decryptWithPublicKey(String cipherText, String senderPublicKey) {
        PublicKey publicKey;
        String decryptedText = "";
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(senderPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            // 암호문 public key 로 복호화
            decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes())));
        }catch(Exception e){
            e.printStackTrace();
        }
        return decryptedText;
    }

    /*
        3. 전자서명과 메시지 원본 합치기
     */
    public String concatResult(String plainText, String digitalSignature){
        StringBuffer sb = new StringBuffer();
        sb.append("-----BEGIN PLAIN TEXT-----\n");
        sb.append(plainText);
        sb.append("-----END PLAIN TEXT-----\n");
        sb.append("-----BEGIN DIGITAL SIGNATURE-----\n");
        sb.append(digitalSignature);
        sb.append("-----END DIGITAL SIGNATURE-----\n");
        return sb.toString();
    }

    /*
        4. 대칭키 생성
     */
    public SecretKey generateSymmetricKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");   // AES Key Generator 객체 생성
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        generator.init(128);    // AES Key size 지정
        SecretKey secKey = generator.generateKey();     // AES 암호화 알고리즘에서 사용할 대칭키 생성
        return secKey;
    }

    /*
        5. 내용물을 대칭키로 암호화
     */
    public String encryptBody(String body, SecretKey secretKey){
        String encryptedData = "";
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteCipherText = aesCipher.doFinal(body.getBytes());    // 암호문 생성
            encryptedData = new String(byteCipherText);
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedData;
    }

    /*
        6. 전자봉투 생성
     */
    public String createEE(SecretKey secretKey, String receiverPublicKey){
        return encryptWithPublicKey(secretKey.getEncoded().toString(), receiverPublicKey);
    }

    /*
        public key로 암호화
     */
    private String encryptWithPublicKey(String data, String receiverPublicKey) {
        String encryptedData = null;
        try{
            // Public key 만들기
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(receiverPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // 암호화 모드 설정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(cipher.ENCRYPT_MODE, publicKey);

            // plainText 암호화
            byte[] byteEncryptedData = cipher.doFinal(data.getBytes());
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedData;
    }

    /*
        private key로 복호화
     */
    private String decryptWithPrivateKey(String cipherText, String receiverPrivateKey){
        String decryptedData = null;
        try {
            // Private Key 객체 생성
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(receiverPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            // 암호화 모드 설정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 암호문 복호화
            byte[] byteEncryptedData = Base64.getDecoder().decode(cipherText.getBytes());
            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);
            decryptedData = new String(byteDecryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }

    /*
        8. 결과물과 전자봉투 합치기
     */
    public String generateFinalResult(String body, String EE){
        StringBuffer sb = new StringBuffer();
        sb.append("-----BEGIN BODY-----\n");
        sb.append(body);
        sb.append("-----END BODY-----\n");
        sb.append("-----BEGIN EE-----\n");
        sb.append(EE);
        sb.append("-----END EE-----\n");
        return sb.toString();
    }


    /*
        데이터 보내기 - PGP 순방향 프로세스
     */
    public String sendData(String plainText){
        String mac = generateMAC(plainText);
        String digitalSignature = encryptMAC(mac);
        String body = concatResult(this.plainText,digitalSignature);
        SecretKey secretKey = generateSymmetricKey();
        String result = encryptBody(body, secretKey);
        String finalResult = createEE(secretKey, this.receiverPublicKey);
        return finalResult;
    }

    /*
        데이터 받기 - PGP 역방향 프로세스
     */
    public String receiveData(String cipherText){


    }
}
