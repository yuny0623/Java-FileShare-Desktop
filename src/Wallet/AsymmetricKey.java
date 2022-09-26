package Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsymmetricKey implements EncKey{
    private String publicKey;
    private String privateKey;
    private String description;

    public AsymmetricKey(String publicKey, String privateKey, String description){
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.description = description;
    }

    @Override
    public HashMap<String, String> getKey() {
        HashMap<String, String> keySet = new HashMap<>();
        keySet.put("publicKey", this.publicKey);
        keySet.put("privateKey", this.privateKey);
        return keySet;
    }

    @Override
    public String getDescription() {
        if(this.description == null){
            return "No description";
        }
        return this.description;
    }
}
