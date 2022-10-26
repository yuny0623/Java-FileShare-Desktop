package org.imageghost.Wallet.MerkleTree;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm {
    public static String generateHash(String originalString){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(originalString.getBytes());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return new String(md.digest());
    }
}
