package org.imageghost;

import org.imageghost.GUIComponents.MyFrame;
import org.imageghost.Server.Connection;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        new MyFrame();

        System.out.println("-----------------------");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ownerPublicKey", "18r1v38cb1o3ryg14o3ggo13ubgh34g13");
        hashMap.put("ciphertext","-01bgbig1p398rg1viufb1kdv1");

        Connection.postRequest("http://localhost:8080/test-post", hashMap);
    }
}
