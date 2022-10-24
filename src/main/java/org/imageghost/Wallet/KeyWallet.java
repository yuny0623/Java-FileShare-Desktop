package org.imageghost.Wallet;

import org.imageghost.ClientCustomException.NoKeyException;
import org.imageghost.Key.ASymmetricKey;
import org.imageghost.Key.SymmetricKey;

import java.util.HashMap;

public class KeyWallet {
    private static HashMap<String, SymmetricKey> symmetricKeyMap = new HashMap<>(); // 대칭키 전용 지갑 (AES)
    private static HashMap<String, ASymmetricKey> asymmetricKeyMap = new HashMap<>(); // 비대칭키 전용 지갑 (RSA)

    private static int numberForSymmetricKey = 0;
    private static int numberForASymmetricKey = 0;

    public static void saveKeyForSymmetricKey(SymmetricKey encKey){
        symmetricKeyMap.put(String.valueOf(++numberForSymmetricKey), encKey);
    }
    public static void saveKeyAsMainKeyForSymmetricKey(SymmetricKey encKey){
        symmetricKeyMap.put("MainKey", encKey);
    }

    public static HashMap<String, SymmetricKey> getAllKeyForSymmetricKey(){
        return symmetricKeyMap;
    }

    public static SymmetricKey getMainKeyForSymmetricKey() throws NoKeyException{
        SymmetricKey findSymmetricKey =  symmetricKeyMap.get("Main");
        if(findSymmetricKey == null){
            throw new NoKeyException("No Main Symmetric Key");
        }else{
            return findSymmetricKey;
        }
    }
    public static void deleteAllKeysForSymmetricKey(){
        symmetricKeyMap.clear();
        numberForSymmetricKey = 0;
    }

    /*
        for Asymmetric key
     */
    public static void saveKeyForASymmetricKey(ASymmetricKey encKey){
        asymmetricKeyMap.put(String.valueOf(++numberForASymmetricKey), encKey);
    }
    public static void saveKeyAsMainKeyForASymmetricKey(ASymmetricKey encKey){
        asymmetricKeyMap.put("Main", encKey);
    }

    public static HashMap<String, ASymmetricKey> getAllKeyForASymmetricKey(){
        return asymmetricKeyMap;
    }

    public static ASymmetricKey getMainKeyForASymmetricKey() throws NoKeyException{
        ASymmetricKey findASymmetricKey =  asymmetricKeyMap.get("Main");
        if(findASymmetricKey == null){
            throw new NoKeyException("No Main ASymmetric Key");
        }else{
            return findASymmetricKey;
        }
    }

    public static void deleteAllKeysForASymmetricKey(){
        asymmetricKeyMap.clear();
        numberForASymmetricKey = 0;
    }
}
