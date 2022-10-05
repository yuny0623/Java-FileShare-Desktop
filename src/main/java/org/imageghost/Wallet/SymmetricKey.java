package org.imageghost.Wallet;

import javax.crypto.SecretKey;
import java.util.HashMap;

public class SymmetricKey{
    private SecretKey AESKey;
    private String description;

    public SymmetricKey(SecretKey aesKey, String description){
        this.AESKey = aesKey;
        this.description = description;
    }

    public SecretKey getKey() {
        return this.AESKey;
    }

    public String getDescription() {
        if(this.description == null){
            return "No description";
        }
        return this.description;
    }
}
