package org.imageghost.OpenChat;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerSocketThread extends Thread{
    Socket socket;
    ChatServer server;
    BufferedReader in;
    PrintWriter out;
    String nickname;
    String threadName;
    String publicKey;

    public ServerSocketThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
        threadName = super.getName();
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
                ChatServer.threadList.put(publicKey, this);
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
                    String receiverPublicKey = strIn.substring(16, strIn.indexOf("]"));
                    server.sendMessageTo(strIn, receiverPublicKey);
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
