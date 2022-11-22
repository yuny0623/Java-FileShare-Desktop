package org.imageghost.Wallet;

import org.imageghost.Key.KeyFactory;
import org.imageghost.Key.Keys.ASymmetricKey;
import org.imageghost.Key.Keys.SymmetricKey;

import java.util.HashMap;

public class KeyWallet implements Wallet{
    private static HashMap<String, SymmetricKey> symmetricKeyMap = new HashMap<>();
    private static HashMap<String, ASymmetricKey> asymmetricKeyMap = new HashMap<>();
    private static SymmetricKey mainSymmetricKey;
    private static ASymmetricKey mainASymmetricKey;

    private static int numberForSymmetricKey = 0;
    private static int numberForASymmetricKey = 0;

    public KeyWallet(){

    }

    public void init() {
        mainASymmetricKey = KeyFactory.createASymmetricKey();
        mainSymmetricKey = KeyFactory.createSymmetricKey();
    }

    public static void saveSymmetricKey(SymmetricKey encKey){
        symmetricKeyMap.put(String.valueOf(numberForSymmetricKey++), encKey);
    }
    public static void saveMainSymmetricKey(SymmetricKey encKey){
        mainSymmetricKey = encKey;
    }
    public static HashMap<String, SymmetricKey> getAllSymmetricKey(){
        return symmetricKeyMap;
    }
    public static SymmetricKey getMainSymmetricKey(){
        if(mainSymmetricKey == null){
            SymmetricKey symmetricKey = KeyFactory.createSymmetricKey();
            mainSymmetricKey = symmetricKey;
        }
        return mainSymmetricKey;
    }
    public static void deleteAllSymmetricKey(){
        symmetricKeyMap.clear();
        numberForSymmetricKey = 0;
    }

    public static void saveASymmetricKey(ASymmetricKey encKey){
        asymmetricKeyMap.put(String.valueOf(numberForASymmetricKey++), encKey);
    }

    public static void saveMainASymmetricKey(ASymmetricKey encKey){
        mainASymmetricKey = encKey;
    }

    public static HashMap<String, ASymmetricKey> getAllASymmetricKey(){
        return asymmetricKeyMap;
    }

    public static ASymmetricKey getMainASymmetricKey(){
        if(mainASymmetricKey == null){
            ASymmetricKey aSymmetricKey = KeyFactory.createASymmetricKey();
            mainASymmetricKey = aSymmetricKey;
        }
        return mainASymmetricKey;
    }

    public static void deleteAllASymmetricKey(){
        asymmetricKeyMap.clear();
        numberForASymmetricKey = 0;
    }

}
