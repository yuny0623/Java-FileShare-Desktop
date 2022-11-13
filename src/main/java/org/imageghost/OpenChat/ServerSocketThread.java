package org.imageghost.OpenChat;

import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ServerSocketThread extends Thread{
    Socket socket;
    ChatServer server;
    BufferedReader in;
    PrintWriter out;
    String name;
    String threadName;

    public ServerSocketThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
        threadName = super.getName();
        System.out.println(socket.getInetAddress() + "님이 입장하였습니다.");
        System.out.println("Thread Name: " + threadName);
    }

    public void sendMessage(String str){
        out.println(str);
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            sendMessage("입력창에 닉네임을 넣으세요");
            name =  in.readLine();
            server.broadCasting("[" + name + "] 님이 입장하였습니다.");
            sendMessage("입력창에 publicKey를 넣으세요");
            String publicKey = in.readLine();
            ChatServer.publicKeyList.put(name, publicKey);
            server.broadCasting("[" + name + "] 님이 준비되었습니다.");

            while(true){
                String strIn = in.readLine();
                if(strIn.charAt(0) == '@'){
                    int tempIdx = strIn.indexOf(':');
                    if(tempIdx == -1){
                        server.broadCasting("[" + name + "]" + strIn);
                    }else{
                        String receiverName = strIn.substring(1, tempIdx);
                        String message = strIn.substring(tempIdx, strIn.length());
                        String receiverPublicKey = ChatServer.publicKeyList.get(receiverName);
                        String encodedMessage = encode(message.getBytes(), receiverPublicKey);
                        server.broadCasting("[" + name + "]" + encodedMessage);
                    }
                }else {
                    server.broadCasting("[" + name + "]" + strIn);
                }
            }
        }catch(IOException e){
            System.out.println(threadName + " 님이 퇴장했습니다.");
            server.removeClient(this);
        }finally {
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public String encode(byte[] plainData, String stringPublicKey) {
        String encryptedData = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] byteEncryptedData = cipher.doFinal(plainData);
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    public byte[] decode(String encryptedData, String stringPrivateKey) {
        byte[] byteDecryptedData = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData.getBytes());
            byteDecryptedData = cipher.doFinal(byteEncryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteDecryptedData;
    }
}
