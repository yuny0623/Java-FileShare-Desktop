package org.imageghost.openchat;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;

public class DirectMessageSocketThread extends Thread{
    Socket socket;
    ChatServer server;
    BufferedReader in;
    PrintWriter out;
    String nickname;
    String threadName;
    String publicKey;

    SecretKey commonSecretKey;
    String senderPublicKey;
    String senderPrivateKey;
    String receiverPublicKey;

    public DirectMessageSocketThread(ChatServer server, Socket socket){
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
    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            while(true){
                String strIn = in.readLine();
                server.broadCasting("[" + nickname + "]" + strIn);

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
