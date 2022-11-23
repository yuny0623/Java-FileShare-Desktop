package org.imageghost.tree.merkletree;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm {
    public static String generateHash(String originalString){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(originalString.getBytes("UTF-8"));
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return new String(Hex.encodeHexString(md.digest()));
    }
}
