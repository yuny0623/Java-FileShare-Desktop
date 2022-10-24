package org.imageghost.Transport.TCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{
    final static int SERVER_PORT = 1225;
    final static String MESSAGE_TO_SERVER = "Hello, Client";


    public String receive(){
        ServerSocket serverSocket = null;
        String response = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (true) {
                System.out.println("socket 연결 대기");
                Socket socket = serverSocket.accept();
                System.out.println("host : "+socket.getInetAddress()+" | 통신 연결 성공");

                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                byte[] data = new byte[16];
                int n = is.read(data);
                final String messageFromClient = new String(data,0,n);

                System.out.println(messageFromClient);

                os.write( MESSAGE_TO_SERVER.getBytes() );
                os.flush();
                response = messageFromClient;
                is.close();
                os.close();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
