package Key;

import java.security.*;
import java.util.Base64;

public class KeyMaker {
    static final int KEY_SIZE = 2048;
    private String privateKey;
    private String publicKey;

    public void generateKeyPair(){
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPublicKey(){
        if(this.publicKey != null){
            return this.publicKey;
        }else{
            throw new NoKeyException("Public Key not existed.");
        }
   }

    public String getPrivateKey(){
        if(this.privateKey != null) {
            return this.privateKey;
        }else{
            throw new NoKeyException("Private Key not existed.");
        }
    }
}
