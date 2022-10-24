package org.imageghost.Test;

import org.imageghost.Config;
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
    public void Mnemonic문자열배열테스트(){
        // given
        int length = Config.MNEMONIC_WORDS.length;

        // when
        System.out.println(length);

        // then
        Assert.assertEquals(length, 2048);
    }
}
