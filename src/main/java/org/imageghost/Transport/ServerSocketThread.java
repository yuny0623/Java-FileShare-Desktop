package org.imageghost.Transport;

import java.io.*;
import java.net.Socket;

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

            sendMessage("대화자 이름을 넣으세요");
            name =  in.readLine();
            server.broadCasting("[" + name + "] 님이 입장하였습니다.");

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
