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
    String nickname;
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

            nickname = in.readLine();
            publicKey = in.readLine();

            if(nickname != null && publicKey != null){
                ChatServer.publicKeyList.put("name", publicKey);
            }else{
                throw new IOException("이름과 publicKey가 없습니다.");
            }
            server.broadCasting("[" + nickname + "]:["+ publicKey +"] 님이 입장하였습니다.");
            while(true){
                String strIn = in.readLine();
                server.broadCastingMessage("[" + nickname + "]" + strIn, publicKey);
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
