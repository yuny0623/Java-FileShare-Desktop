package org.imageghost.GUIComponents;

import org.imageghost.AesEncryption.AESCipherMaker;
import org.imageghost.SecureAlgorithm.PGP.PGP;
import org.imageghost.SecureAlgorithm.Utils.MessageInput;
import org.imageghost.SecureAlgorithm.Utils.MessageOutput;
import org.imageghost.Wallet.KeyWallet;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class DirectMessageGui extends JFrame implements ActionListener, Runnable {

    Container container = getContentPane();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane;
    JTextField textField = new JTextField();
    PGP pgp;

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String str;
    String receiverPublicKey;
    SecretKey commonSecretKey;

    String nickname;
    String publicKey;
    String privateKey;
    String symmetricKey;

    public DirectMessageGui(String ip, int port, Socket socket, String receiverPublicKey){
        setTitle("DirectMessage");
        setSize(500, 500);
        setLocation(300, 300);
        init();
        start();
        setVisible(true);
        this.socket = socket;
        this.receiverPublicKey = receiverPublicKey;
        initNet(ip, port);
        System.out.println("ip = " + ip);
    }

    public void initNet(String ip, int port){
        try{
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        }catch(UnknownHostException e){
            System.out.println("IP주소가 다릅니다. ");
        }catch(IOException e){
            System.out.println("접속 실패");
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    public void init(){
        container.setLayout(new BorderLayout());
        scrollPane = new JScrollPane(textArea);
        container.add("Center", scrollPane);
        container.add("South", textField);
    }

    public void start(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textField.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(commonSecretKey == null) {
            str = textField.getText();
            MessageInput messageInput = pgp.send(str);
            if (messageInput.isError()) {
                str = messageInput.getErrorMessage();
            } else {
                out.println(messageInput.getCipherText());
                textField.setText("");
            }
        }else{
            str = textField.getText();
            try {
                out.println(AESCipherMaker.decrypt(str.getBytes(), commonSecretKey));
                textField.setText("");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void run() {
        pgp = new PGP();
        pgp.setReceiverPublicKey(this.receiverPublicKey);
        pgp.setSenderPrivateKey(KeyWallet.getMainASymmetricKey().getPrivateKey());
        pgp.setSenderPublicKey(KeyWallet.getMainASymmetricKey().getPublicKey());

        while(true){
            try{
                str = in.readLine();
                MessageOutput messageOutput = pgp.receive(str);
                commonSecretKey = pgp.decryptedSecretKey;
                if(messageOutput.isError()){
                    System.out.println(messageOutput.getErrorMessage());
                    continue;
                }
                if(!messageOutput.isIntegrity()){
                    System.out.println("Message Integrity failed.");
                    continue;
                }
                textArea.append(messageOutput.getPlainText() + "\n");
                if(commonSecretKey!=null){
                    break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        while(true){
            try {
                str = in.readLine();
                String receivedPlainText = AESCipherMaker.encrypt(str, commonSecretKey);
                textArea.append(receivedPlainText + "\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
