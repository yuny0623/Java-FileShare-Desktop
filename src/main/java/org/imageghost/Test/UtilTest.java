package org.imageghost.Test;

import org.imageghost.Config;
import org.imageghost.Key.KeyFactory;
import org.imageghost.Key.Keys.ASymmetricKey;
import org.imageghost.SecureAlgorithm.Utils.RSAUtil;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void contains메소드테스트(){
        // given
        String temp = "안녕하세요. 테스트 문자열입니다. 반갑습니다.";

        // when
        String subString = "요. 테스트 문";

        // then
        Assert.assertTrue(temp.contains(subString));
    }

    @Test
    public void Mnemonic문자열array테스트(){
        // given
        int length = Config.MNEMONIC_WORDS.length;

        // when

        // then
        Assert.assertEquals(length, 2048);
    }

    @Test
    public void RSAUtil_암복호화테스트(){
        // given
        ASymmetricKey aSymmetricKey = KeyFactory.createAsymmetricKey();
        String privateKey = aSymmetricKey.getPrivateKey();
        String publicKey = aSymmetricKey.getPublicKey();

        String plainText = "this is test.";

        // when
        String cipherText = RSAUtil.encode(plainText.getBytes(), publicKey);
        byte[] decryptedText = RSAUtil.decode(cipherText, privateKey);

        // then
        System.out.println(decryptedText);
        Assert.assertEquals(plainText, decryptedText.toString());
    }
}
