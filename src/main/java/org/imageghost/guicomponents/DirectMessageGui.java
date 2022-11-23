package org.imageghost.guicomponents;

import org.imageghost.aesencryption.AESCipherMaker;
import org.imageghost.securealgorithm.pgp.PGP;
import org.imageghost.securealgorithm.utils.MessageInput;
import org.imageghost.securealgorithm.utils.MessageOutput;
import org.imageghost.wallet.KeyWallet;

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
    String receiverNickname;

    public DirectMessageGui(Socket socket, String receiverNickname, String receiverPublicKey){
        this.socket = socket;
        this.receiverNickname = receiverNickname;
        this.receiverPublicKey = receiverPublicKey;

        this.pgp = new PGP();
        this.pgp.setReceiverPublicKey(receiverPublicKey);
        this.pgp.setSenderPrivateKey(KeyWallet.getMainASymmetricKey().getPrivateKey());
        this.pgp.setSenderPublicKey(KeyWallet.getMainASymmetricKey().getPublicKey());

        setTitle("DirectMessage");
        setSize(300, 300);
        setLocation(300, 600);
        init();
        start();
        setVisible(true);
        initNet();
    }

    public void initNet(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        }catch(UnknownHostException e){
            System.out.println("Different IP Address.");
        }catch(IOException e){
            System.out.println("Connection failed.");
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    public void init(){
        container.setLayout(new BorderLayout());
        scrollPane = new JScrollPane(textArea);
        container.add("Center", scrollPane);
        container.add("South", textField);

        JMenuBar menuBar = new JMenuBar();
        JMenu directMessageMenu = new JMenu("[MyDirectMessage]");
        JMenuItem menuItem = new JMenuItem(new AbstractAction("[MyDirectMessage]") {
            public void actionPerformed(ActionEvent e) {
                String request = "[MyDirectMessage]";
                out.println(request);
            }
        });

        directMessageMenu.add(menuItem);
        menuBar.add(directMessageMenu);
        this.setJMenuBar(menuBar);
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
                textArea.setText("Error: " + str + '\n');
                textField.setText("");
            } else {
                out.println("[DirectMessageTo:" + receiverNickname + "]" + messageInput.getCipherText());
                textField.setText("");
            }
        }else{
            str = textField.getText();
            try {
                out.println("[DirectMessageTo:" + receiverNickname + "]" + AESCipherMaker.encryptWithBase64(str, commonSecretKey));
                textField.setText("");
            } catch (Exception ex) {
                textArea.setText(ex.getMessage());
                textField.setText("");
            }
        }
    }

    @Override
    public void run() {
        while(true){
            try{
                str = in.readLine();
                if(str==null){
                    continue;
                }
                MessageOutput messageOutput = pgp.receive(str);
                commonSecretKey = pgp.decryptedSecretKey;
                if(messageOutput.isError()){
                    textArea.setText("[Error]:"+messageOutput.getErrorMessage());
                    continue;
                }
                if(!messageOutput.isIntegrity()){
                    textArea.setText("[Integrity Error]:"+messageOutput.isIntegrity());
                    continue;
                }
                textArea.append("messageOutput:" + messageOutput.getPlainText() + "\n");
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
                if(str == null){
                    continue;
                }
                String receivedPlainText = AESCipherMaker.decryptWithBase64(str, commonSecretKey);
                textArea.append("messageOutput: " + receivedPlainText + "\n");
            } catch (Exception e) {
                textArea.setText("[Error]: "+ e.getMessage());
            }
        }
    }
}
