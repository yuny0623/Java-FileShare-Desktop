package org.imageghost.Test;

import org.imageghost.AsymmetricKey.AsymmetricKeyMaker;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class MnemonicTest {
    @Test
    public void privatekey_publickey_길이_테스트(){
        // given
        HashMap<String, String> keyPair = AsymmetricKeyMaker.generateKeyPair();
        String privateKey = keyPair.get("privateKey");
        String publicKey = keyPair.get("publicKey");

        // when
        int privateKeyLength = privateKey.length();
        int publicKeyLength = publicKey.length();

        // then
        Assert.assertEquals(2048, privateKeyLength);
        Assert.assertEquals(2048, publicKeyLength);
    }
}
