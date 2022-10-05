package org.imageghost.Wallet;

import java.util.ArrayList;
import java.util.List;

public class ASymmetricKey {
    private String description;
    private List<String> keyPair = new ArrayList<>();

    public ASymmetricKey(String publicKey, String privateKey){
        this.keyPair.add(publicKey);
        this.keyPair.add(privateKey);
    }

    public String getDescription() {
        return this.description;
    }

    public String getPublicKey(){
        return this.keyPair.get(0);
    }

    public String getPrivateKey(){
        return this.keyPair.get(1);
    }

    public void deleteKeyPair(){ // key pair 삭제
        keyPair.clear();
    }
}
