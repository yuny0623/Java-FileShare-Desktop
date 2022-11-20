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
    String myNickname;
    String opponentNickname;

    public DirectMessageGui(Socket socket, String receiverPublicKey){
        System.out.printf("DirectMessage socket info: %s\n", socket.getInetAddress());
        setTitle("DirectMessage");
        setSize(300, 300);
        setLocation(300, 300);
        init();
        start();
        setVisible(true);
        this.socket = socket;
        this.receiverPublicKey = receiverPublicKey;

        this.pgp = new PGP();
        this.pgp.setReceiverPublicKey(this.receiverPublicKey);
        this.pgp.setSenderPrivateKey(KeyWallet.getMainASymmetricKey().getPrivateKey());
        this.pgp.setSenderPublicKey(KeyWallet.getMainASymmetricKey().getPublicKey());

        initNet();
    }

    public void initNet(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        }catch(UnknownHostException e){
            System.out.println("IP 주소가 다릅니다. ");
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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
                out.println("[DirectMessageTo:"+receiverPublicKey+"]" + messageInput.getCipherText());
                textField.setText("");
            }
        }else{
            str = textField.getText();
            try {
                out.println("[DirectMessageTo:"+receiverPublicKey+"]" + AESCipherMaker.encryptWithBase64(str, commonSecretKey));
                textField.setText("");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void run() {
        while(true){
            System.out.println("DirectMessage is running... first loop");
            try{
                str = in.readLine();
                MessageOutput messageOutput = pgp.receive(str);
                commonSecretKey = pgp.decryptedSecretKey;
                if(messageOutput.isError()){
                    continue;
                }
                if(!messageOutput.isIntegrity()){
                    continue;
                }
                System.out.printf("messageOutput.getPlainText: %s\n", messageOutput.getPlainText());
                textArea.append("messageOutput:" + messageOutput.getPlainText() + "\n");
                if(commonSecretKey!=null){
                    break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        while(true){
            System.out.println("DirectMessage is running... second loop");
            try {
                str = in.readLine();
                String receivedPlainText = AESCipherMaker.decryptWithBase64(str, commonSecretKey);
                System.out.printf("receivedPlainText: %s\n", receivedPlainText);
                textArea.append(receivedPlainText + "\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
