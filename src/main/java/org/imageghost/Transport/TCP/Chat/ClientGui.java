package org.imageghost.Transport.TCP.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientGui extends JFrame implements ActionListener, Runnable {

    Container container = getContentPane();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    JTextField textField = new JTextField();

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String str;

    public ClientGui(String ip, int port){
        setTitle("Chatting");
        setSize(550, 400);
        setLocation(400, 400);
        init();
        start();
        setVisible(true);
        initNet(ip, port);
        System.out.println("ip = " + ip);
    }

    public void initNet(String ip, int port){
        try{
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =  new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        }catch(UnknownHostException e){
            System.out.println("IP 주소가 다릅니다.");
        }catch(IOException e){
            System.out.println("접속 실패");
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    public void init(){
        container.setLayout(new BorderLayout());
        container.add("Center", scrollPane);
        container.add("South", textField);
    }

    public void start(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textField.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        str = textField.getText();
        out.println(str);
        textField.setText("");
    }

    @Override
    public void run() {
        while(true){
            try{
                str = in.readLine();
                textArea.append(str + "\n");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
