package org.imageghost.OpenChat;

import org.imageghost.SecureAlgorithm.Utils.RSAUtil;
import org.imageghost.Wallet.KeyWallet;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class ServerSocketThread extends Thread{
    Socket socket;
    ChatServer server;
    BufferedReader in;
    PrintWriter out;
    String name;
    String threadName;
    String publicKey;

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
        int i = 0;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            name = in.readLine();
            publicKey = in.readLine();

            if(name != null && publicKey != null){
                ChatServer.publicKeyList.put("name", publicKey);
            }else{
                throw new IOException("이름과 publicKey가 없습니다.");
            }

            server.broadCasting("[" + name + "]:["+ publicKey +"] 님이 입장하였습니다.");
            while(true){
                String strIn = in.readLine();
                server.broadCasting("[" + name + "]" + strIn);
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
}

/*
                if(strIn.charAt(0) == '@'){
                    int tempIdx = strIn.indexOf(':');
                    if(tempIdx == -1){
                        server.broadCasting("[" + name + "]" + strIn);
                    }else{
                        String receiverName = strIn.substring(1, tempIdx);
                        String message = strIn.substring(tempIdx, strIn.length());
                        String receiverPublicKey = ChatServer.publicKeyList.get(receiverName);
                        String encodedMessage = RSAUtil.encode(message.getBytes(), receiverPublicKey);
                        server.broadCasting("[" + name + "]" + encodedMessage);
                    }
                }else {
                    server.broadCasting("[" + name + "]" + strIn);
                }
 */