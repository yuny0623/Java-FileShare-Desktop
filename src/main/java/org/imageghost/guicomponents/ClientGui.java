package org.imageghost.guicomponents;

import org.imageghost.securealgorithm.pgp.PGP;
import org.imageghost.wallet.KeyWallet;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class ClientGui extends JFrame implements ActionListener, Runnable {
    Container container = getContentPane();
    JTextArea textArea = new JTextArea();

    JTextArea userInfoTextArea = new JTextArea();
    JTextArea directMessageTextArea = new JTextArea();
    JTextField inputField = new JTextField();

    PGP pgp;

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String str;

    String nickname;
    String publicKey;
    String privateKey;

    HashMap<String, String> userMap = new HashMap<>();         // nickname, publicKey
    HashMap<String, SecretKey> commonKeyMap = new HashMap<>(); // nickname, commonKey -> for DirectMessage

    public ClientGui(String ip, int port){
        nickname = JOptionPane.showInputDialog("Enter User Nickname");
        publicKey = KeyWallet.getMainASymmetricKey().getPublicKey();
        privateKey = KeyWallet.getMainASymmetricKey().getPrivateKey();
        userMap.put(nickname, publicKey);

        setTitle("Chatting");
        setSize(300, 300);
        setLocationRelativeTo(null);

        start();
        setVisible(true);
        initNet(ip, port);
        init();
        System.out.println("ip = " + ip);
    }

    public void initNet(String ip, int port){
        try{
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =  new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        }catch(UnknownHostException e){
            System.out.println("Different Ip Address");
        }catch(IOException e){
            System.out.println("Connection failed.");
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    public void init(){
        getContentPane().setLayout(null);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 0));

        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        JButton jButton3 = new JButton();
        JButton jButton4 = new JButton();

        centerPanel.add(jButton1, BorderLayout.CENTER);
        directMessageTextArea.setBounds(0, 0, 100, 100);
        centerPanel.add(jButton2, BorderLayout.EAST);
        centerPanel.add(jButton3, BorderLayout.WEST);
        centerPanel.add(jButton4, BorderLayout.SOUTH);
        centerPanel.setBounds(0, 0, 300, 300);

        container.add(centerPanel);
    }

    /*
    public void init(){
        container.setLayout(new BorderLayout());
        scrollPane = new JScrollPane(textArea);

        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        JMenuItem menuItem1 = new JMenuItem(new AbstractAction("UserInfoRequest") {
            public void actionPerformed(ActionEvent e) {
                String request = "[userInfoRequest]";
                out.println(request);
            }
        });

        JMenuItem menuItem2 = new JMenuItem(new AbstractAction("UserInfoResponse") {
            public void actionPerformed(ActionEvent e) {
                StringBuffer sb = new StringBuffer();
                for(Map.Entry<String, String> entry: userMap.entrySet()){
                    sb.append("[" + entry.getKey() +":"+ entry.getValue()+"]\n");
                }
                new UserInfoResponseGui(sb.toString());
            }
        });

        JMenuItem menuItem3 = new JMenuItem(new AbstractAction("DirectMessage") {
            public void actionPerformed(ActionEvent e) {
                String receiverPublicKey = JOptionPane.showInputDialog(null, "Receiver PublicKey", "publicKey");
                String receiverNickname = JOptionPane.showInputDialog(null, "Receiver Nickname", "Nickname");
                new DirectMessageGui(socket, receiverNickname, receiverPublicKey);
            }
        });

        southPanel.add(inputField, BorderLayout.CENTER);     // 입력창
        centerPanel.add(scrollPane, BorderLayout.CENTER);    // 채팅 출력창
        southPanel.setSize(new Dimension(300, 300));

        container.add(northPanel, BorderLayout.NORTH);
        container.add(southPanel, BorderLayout.SOUTH);
        container.add(westPanel, BorderLayout.WEST);
        container.add(eastPanel, BorderLayout.EAST);
        container.add(centerPanel, BorderLayout.CENTER);

        JTextArea userInfo = new JTextArea("UserInfo");
        JScrollPane jScrollPane = new JScrollPane(userInfo);
        JPanel jpanel = new JPanel();
        jpanel.add(jScrollPane);
    }
     */

    public void start(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        inputField.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        str = inputField.getText();
        out.println(str);
        inputField.setText("");
    }

    @Override
    public void run() {
        try {
            out.println(nickname);
            out.println(publicKey);
        }catch(NullPointerException e){
            textArea.setText("[Error - Server is not running]: " + e.getMessage());
        }

        while(true){
            try{
                str = in.readLine();
                if(str == null){
                    continue;
                }
                if(str.length() >= 18 && str.substring(0, 17 + 1).equals("[userInfoResponse]")){
                    String[] info = str.split(" ");
                    for(int i = 1; i < info.length; i+=2){
                        userMap.put(info[i], info[i+1]);
                    }
                    continue;
                }else if(str.length() >= 11 && str.substring(0, 11 + 1).equals("[New Member]")){
                    String strBody = str.substring(11+1, str.length()); // [sss:aaa]
                    String receivedNickname = strBody.substring(1, strBody.indexOf(":"));
                    String receivedPublicKey = strBody.substring(strBody.indexOf(":") + 1, strBody.indexOf("]"));
                    userMap.put(receivedNickname, receivedPublicKey);
                    continue;
                }
                textArea.append(str+ "\n");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
