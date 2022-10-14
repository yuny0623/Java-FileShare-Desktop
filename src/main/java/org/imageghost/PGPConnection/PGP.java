package org.imageghost.PGPConnection;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PGP {
    /*
        PGP Communication Structure Implementation - by yuny0623
     */
    private String senderPublicKey;
    private String senderPrivateKey;
    private String receiverPublicKey;
    public PGP( String senderPublicKey, String senderPrivateKey, String receiverPublicKey){
        this.senderPublicKey = senderPublicKey;
        this.senderPrivateKey = senderPrivateKey;
        this.receiverPublicKey = receiverPublicKey;
    }

    /*
        1: MAC 생성
     */
    public String generateMAC(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(plainText.getBytes());
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
        2
     */
    public String encryptMAC(String MAC){
        /*
            encrypt MAC via private key of Alice.
         */
        return encryptWithPrivateKey(MAC, this.senderPrivateKey);
    }

    /*
        private key 로 전자서명 사인
     */
    public String encryptWithPrivateKey(String plainText, String senderPrivateKey) {
        /*
            private key로 암호화 진행. ? 여기서 이슈 발생. 어떻게 private key 로 encryption 을 진행할까요?
         */

        // digital signature 과정의 일부인데... digital signature 과정만 다른 방식으로 대체해도 될듯.
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
        3
     */
    public String concatResult(String plainText, String digitalSig){
        return new String("");
    }

    /*
        4
     */
    public String generateSymmetricKey(){
        return new String("");

    }

    /*
        5
     */
    public String encryptBody(String symmetricKey){
        return new String("");

    }

    /*
        6 - 7
     */
    public String createEE(String symmetricKey, String body){
        return new String("");

    }

    /*
        8
     */
    public String generateFinalResult(String body, String EE){
        return new String("");

    }


    /*
        Alice:
           1. generate MAC
           2. encrypt MAC with Alice's private key
           3. add original message to result of 2
           4. generate new symmetric key
           5. encrypt 3 with the result of 4. the symmetric key
           6. put symmetric key in E.E
           7. encrypt E.E with Bob's public key
           8. add all result and send to Bob

        Bob:
            reverse it.
     */


    /*
        데이터 보내기
     */
    public void send(String plainText){
        /*
            PGP 순방향 프로세스
         */
    }

    /*
        데이터 받기
     */
    public void receive(String cipherText){
        /*
            PGP 역방향 프로세스
         */
    }
}
