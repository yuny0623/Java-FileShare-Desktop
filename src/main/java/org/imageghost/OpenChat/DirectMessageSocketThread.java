package org.imageghost.OpenChat;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;

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
        /*
            Direct Message logic
         */
    }
}
