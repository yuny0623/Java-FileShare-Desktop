package org.imageghost.GUIComponents;

import org.imageghost.SecureAlgorithm.Utils.RSAUtil;
import org.imageghost.Wallet.KeyWallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientGui extends JFrame implements ActionListener, Runnable {

    Container container = getContentPane();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane;
    JTextField textField = new JTextField();
    JMenuBar menuBar;
    JMenu roomMenu;

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String str;

    String nickname;
    String publicKey;
    String privateKey;

    HashMap<String, String> userMap = new HashMap<>();

    public ClientGui(String ip, int port){
        nickname = JOptionPane.showInputDialog("닉네임을 입력하세요");
        publicKey = KeyWallet.getMainASymmetricKey().getPublicKey();
        privateKey = KeyWallet.getMainASymmetricKey().getPrivateKey();

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
        scrollPane = new JScrollPane(textArea);

        menuBar = new JMenuBar();
        roomMenu = new JMenu("Room");
        JMenuItem menuItem1 = new JMenuItem(new AbstractAction("[userInfoRequest]") {
            public void actionPerformed(ActionEvent e) {
                String request = "[userInfoRequest]";
                System.out.println(request);
                out.println(request);
            }
        });
        JMenuItem menuItem2 = new JMenuItem(new AbstractAction("[userInfoResponse]") {
            public void actionPerformed(ActionEvent e) {
                StringBuffer sb = new StringBuffer();
                for(Map.Entry<String, String> entry: userMap.entrySet()){
                    sb.append("[" + entry.getKey() +":"+ entry.getValue()+"]\n");
                }
                new UserInfoResponseGui(sb.toString());
            }
        });
        JMenuItem menuItem3 = new JMenuItem(new AbstractAction("[DirectMessage]") {
            public void actionPerformed(ActionEvent e) {
                /*
                    Direct message 구현
                 */
            }
        });

        roomMenu.add(menuItem1);
        roomMenu.add(menuItem2);
        roomMenu.add(menuItem3);
        menuBar.add(roomMenu);

        this.setJMenuBar(menuBar);
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
        textArea.append("서버로 닉네임과 pubilcKey를 전송합니다.\n");
        out.println(nickname);
        out.println(publicKey);

        while(true){
            try{
                str = in.readLine();
                if(str.length() >= 18 && str.substring(0, 17 + 1).equals("[userInfoResponse]")){
                    String[] info = str.split(" ");
                    System.out.printf("this is the string received from server: " + str);
                    for(int i = 1; i < info.length; i+=2){
                        userMap.put(info[i], info[i+1]);
                    }
                    continue;
                }
                textArea.append(str + "\n");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
