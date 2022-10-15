package org.imageghost.Test;

import org.imageghost.PGPConnection.PGP;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

public class KeyTest {

    @Test
    public void 대칭키생성테스트(){

        // given
        PGP pgp = new PGP("", "", "");

        // when
        SecretKey secretKey = pgp.generateSymmetricKey();

        //then
        Assert.assertNotNull(secretKey);
        /*
        byte[]:
        [B@67b64c45
        [B@566776ad
        [B@67b64c45
        [B@6b2fad11
        [B@3cda1055
        [B@35f983a6

        string:
        [B@35f983a6
        [B@566776ad
         */
    }
}
