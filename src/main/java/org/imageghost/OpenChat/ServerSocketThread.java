package org.imageghost.OpenChat;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ServerSocketThread extends Thread{
    Socket socket;
    ChatServer server;
    BufferedReader in;
    PrintWriter out;
    String nickname;
    String threadName;
    String publicKey;
    SecretKey commonSecretKey;
    Queue<HashMap<String, String>> messageQueue;

    public ServerSocketThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
        threadName = super.getName();
        messageQueue = new LinkedList<>();
        System.out.println(socket.getInetAddress() + ": entered.");
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

            nickname = in.readLine();
            publicKey = in.readLine();

            if(nickname != null && publicKey != null){
                ChatServer.publicKeyList.put(nickname, publicKey);
                ChatServer.threadList.put(nickname, this);
            }else{
                throw new IOException("No Nickname and No PublicKey");
            }

            server.broadCasting("[" + nickname + "] entered.");

            while(true){
                String strIn = in.readLine();
                if(strIn.length() >= 17 && strIn.substring(0, 16 + 1).equals("[userInfoRequest]")){
                    StringBuffer sb = new StringBuffer();
                    for(Map.Entry<String, String> entry: ChatServer.publicKeyList.entrySet()){
                        sb.append(entry.getKey() + " ");
                        sb.append(entry.getValue() + " ");
                    }
                    sendMessage("[userInfoResponse] " + sb.toString());
                } else if(strIn.length() >= 17 && strIn.substring(0, 16 + 1).equals("[DirectMessageTo:")){
                    String nickname = strIn.substring(16 + 1, strIn.indexOf("]"));
                    String receivedMessage = strIn.substring(strIn.indexOf("]") + 1, strIn.length());
                    System.out.println("strIn: " + strIn);
                    System.out.println("nickname:" + nickname);
                    System.out.println("receivedMessage: " + receivedMessage);
                    server.sendDirectMessage(receivedMessage, nickname, this.nickname);
                } else if(strIn.equals("[MyDirectMessage]")) {
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0; i < messageQueue.size(); i++){
                        HashMap<String, String> val = messageQueue.poll();
                        sb.append(val.toString());
                    }
                    sendMessage("[MyDirectMessage] " + sb.toString());
                } else {
                    server.broadCasting("[" + nickname + "]" + strIn);
                }
            }
        }catch(IOException e){
            System.out.println(threadName + ": removed.");
            server.removeClient(this);
        }finally {
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
