package org.imageghost.Key;

public class MnemonicGenerator {
    /*
        Mnemonic Generator
        - private key 기반으로 생성하기.
     */

    public MnemonicGenerator(){
        // 1000 size 로 매핑
    }

    /*
        private key로부터 Mnemonic 생성
     */
    public String[] getMenemonic(String privateKey){
        return new String[1];
    }

    /*
        mnemonic 으로부터 private key 복구
     */
    public String getPrivateKeyFromMnemonic(String[] mnemonic){
        return new String();
    }
}
