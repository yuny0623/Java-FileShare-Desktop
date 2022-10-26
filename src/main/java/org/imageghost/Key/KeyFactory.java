package org.imageghost.Key;

public class KeyFactory {
    /*
        static factory method pattern for Key.
     */
    public static Key createSymmetricKey(){
        return new ASymmetricKey();
    }

    public static Key createAsymmetricKey(){
        return new SymmetricKey();
    }
}
