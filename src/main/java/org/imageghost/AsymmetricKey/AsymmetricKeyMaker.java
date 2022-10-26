package org.imageghost.AsymmetricKey;

import java.security.*;
import java.util.Base64;
import java.util.HashMap;

public class AsymmetricKeyMaker {
    private static final int KEY_SIZE = 2048;
    private static String privateKey;
    private static String publicKey;

    public static HashMap<String, String> generateKeyPair(){
        HashMap<String, String> keyPairHashMap = new HashMap<>();
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            AsymmetricKeyMaker.publicKey = stringPublicKey;
            AsymmetricKeyMaker.privateKey = stringPrivateKey;

            keyPairHashMap.put("publicKey", stringPublicKey);
            keyPairHashMap.put("privateKey", stringPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyPairHashMap;
    }
}
