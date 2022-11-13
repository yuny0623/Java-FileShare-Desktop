package org.imageghost.Wallet;

import org.imageghost.Key.Keys.ASymmetricKey;
import org.imageghost.Key.Keys.SymmetricKey;

import java.util.HashMap;

public class KeyWallet implements Wallet{
    private static HashMap<String, SymmetricKey> symmetricKeyMap = new HashMap<>();
    private static HashMap<String, ASymmetricKey> asymmetricKeyMap = new HashMap<>();

    SymmetricKey mainSymmetricKey;
    ASymmetricKey mainASymmetricKey;

    private static int numberForSymmetricKey = 0;
    private static int numberForASymmetricKey = 0;

    public static void saveSymmetricKey(SymmetricKey encKey){
        symmetricKeyMap.put(String.valueOf(numberForSymmetricKey++), encKey);
    }
    public static void saveMainSymmetricKey(SymmetricKey encKey){
        symmetricKeyMap.put("MainKey", encKey);
    }
    public static HashMap<String, SymmetricKey> getAllSymmetricKey(){
        return symmetricKeyMap;
    }
    public static SymmetricKey getMainSymmetricKey(){
        SymmetricKey findSymmetricKey =  symmetricKeyMap.get("MainKey");
        return findSymmetricKey;
    }
    public static void deleteAllSymmetricKey(){
        symmetricKeyMap.clear();
        numberForSymmetricKey = 0;
    }

    public static void saveASymmetricKey(ASymmetricKey encKey){
        asymmetricKeyMap.put(String.valueOf(numberForASymmetricKey++), encKey);
    }
    public static void saveMainASymmetricKey(ASymmetricKey encKey){
        asymmetricKeyMap.put("MainKey", encKey);
    }

    public static HashMap<String, ASymmetricKey> getAllASymmetricKey(){
        return asymmetricKeyMap;
    }

    public static ASymmetricKey getMainASymmetricKey(){
        ASymmetricKey findASymmetricKey =  asymmetricKeyMap.get("MainKey");
        return findASymmetricKey;
    }

    public static void deleteAllASymmetricKey(){
        asymmetricKeyMap.clear();
        numberForASymmetricKey = 0;
    }
}
