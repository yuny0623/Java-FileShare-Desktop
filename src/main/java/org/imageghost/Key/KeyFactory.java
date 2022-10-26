package org.imageghost.Key;

public class KeyFactory {
    /*
        static factory method pattern for Key.
     */
    public static SymmetricKey createSymmetricKey(){
        return new SymmetricKey();
    }

    public static ASymmetricKey createAsymmetricKey(){
        return new ASymmetricKey();
    }
}
