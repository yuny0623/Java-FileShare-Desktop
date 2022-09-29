package Wallet;

import ClientCustomException.NoKeyException;

import java.util.HashMap;

public class KeyWallet {
    private static HashMap<String, SymmetricKey> symmetricKeyMap = new HashMap<>(); // 대칭키 전용 지갑 (AES)
    private static HashMap<String, ASymmetricKey> asymmetricKeyMap = new HashMap<>(); // 비대칭키 전용 지갑 (RSA)
    private static int number = 0;

    public static void saveKey(SymmetricKey encKey){
        symmetricKeyMap.put(String.valueOf(++number), encKey);
    }
    public static void saveKeyAsMainKey(SymmetricKey encKey){
        symmetricKeyMap.put("MainKey", encKey);
    }

    public static HashMap<String, SymmetricKey> getAllKey(){
        return symmetricKeyMap;
    }

    public static SymmetricKey getMainKey() throws NoKeyException{
        SymmetricKey findSymmetricKey =  symmetricKeyMap.get("Main");
        if(findSymmetricKey == null){
            throw new NoKeyException("No Main Symmetric Key");
        }else{
            return findSymmetricKey;
        }
    }

    public static void deleteAllKeys(){
        symmetricKeyMap.clear();
        number = 0;
    }
}
