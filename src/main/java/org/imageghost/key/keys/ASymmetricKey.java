package org.imageghost.key.keys;

import java.security.*;
import java.util.Base64;
import java.util.HashMap;

public class ASymmetricKey implements Key{
    private final int KEY_SIZE = 2048;
    private HashMap<String, String> keyMap = new HashMap<>();
    private String content;
    private String publicKey;
    private String privateKey;

    public ASymmetricKey(){
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            this.publicKey = stringPublicKey;
            this.privateKey = stringPrivateKey;

            keyMap.put("publicKey", stringPublicKey);
            keyMap.put("privateKey", stringPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getPublicKey(){
        return this.publicKey;
    }

    public String getPrivateKey(){
        return this.privateKey;
    }

    @Override
    public Object getKey() {
        return this.keyMap;
    }
}
