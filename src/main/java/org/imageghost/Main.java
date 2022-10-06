package org.imageghost;

import org.imageghost.GUIComponents.MyFrame;
import org.imageghost.Server.Connection;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception{
        new MyFrame();
//
//        System.out.println("----------POST test---------");
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("ownerPublicKey", "18r1v38cb1o3ryg14o3ggo13ubgh34g13");
//        hashMap.put("ciphertext","01bgbig1p398rg1viufb1kdv1");
//        Connection.httpPostRequest(Config.SERVER_URL + "/test-post", hashMap);
//
//        System.out.println("----------GET test---------");
//        String result = Connection.httpGetRequest(Config.SERVER_URL + "/test-get/", "18r1v38cb1o3ryg14o3ggo13ubgh34g13");
//        System.out.println("GET result: " + result);
    }
}
