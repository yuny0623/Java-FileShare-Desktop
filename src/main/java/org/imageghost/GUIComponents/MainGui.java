package org.imageghost.GUIComponents;

import org.imageghost.CustomException.NoKeyException;
import org.imageghost.CustomException.NoServerException;
import org.imageghost.Config;
import org.imageghost.Key.Keys.ASymmetricKey;
import org.imageghost.Key.KeyFactory;
import org.imageghost.Key.Keys.SymmetricKey;
import org.imageghost.ImageConverter.AESFileTranslator;
import org.imageghost.HTTP.HTTPConnection;
import org.imageghost.Wallet.KeyWallet;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MainGui extends JFrame implements ActionListener {
    JButton button0 = null; 
    JButton button1 = null;
    JButton button2 = null;
    JButton button3 = null;
    JButton button4 = null;

    JTextField textField1 = null;
    JTextField textField2 = null;
    JTextField textField3 = null;
    JTextField textField4 = null;

    JTextArea textArea1 = null;
    JTextArea textArea2 = null;
    JPanel panel0 = null;
    JPanel panel1 = null;
    JPanel panel2 = null;

    public MainGui(){
        setTitle("ImageGhostClient");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.DARK_GRAY);

        panel0 = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();

        button0 = new JButton("Check Server Status");
        button1 = new JButton("Create Symmetric key");
        button2 = new JButton("Create ASymmetric key");
        button3 = new JButton("Send to server");
        button4 = new JButton("Get File From Server");

        // Server Check 전까지는 버튼 사용 불가
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);

        button0.setBackground(Color.GRAY);
        button1.setBackground(Color.GRAY);
        button2.setBackground(Color.GRAY);
        button3.setBackground(Color.GRAY);
        button4.setBackground(Color.GRAY);

        button0.addActionListener(this);
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        textField1 = new JTextField();
        textArea1 = new JTextArea("Type file path here.", 10, 20);
        textArea2 = new JTextArea("Result status,", 10, 40);

        panel0.add(button0);
        panel0.setBackground(Color.DARK_GRAY) ;

        panel1.add(button1);
        panel1.add(button2);
        panel1.add(button3);
        panel1.add(button4);
        panel1.setBackground(Color.DARK_GRAY);

        JPanel jpanelParent = new JPanel();
        JPanel jpanelLeft = new JPanel();
        JPanel jpanelRight = new JPanel();

        JTextField jTextField1 = new JTextField();
        jTextField1.setText("Type File Path here.");
        jpanelLeft.add(jTextField1, BorderLayout.NORTH);
        jpanelLeft.add(textArea1, BorderLayout.CENTER);

        JTextField jTextField2 = new JTextField();
        jTextField2.setText("Result");
        jpanelRight.add(jTextField2, BorderLayout.NORTH);
        jpanelRight.add(textArea2, BorderLayout.CENTER);

        jpanelParent.add(jpanelLeft);
        jpanelParent.add(jpanelRight);

        jpanelParent.setBackground(Color.DARK_GRAY);

        this.add(panel0, BorderLayout.NORTH);
        this.add(panel1, BorderLayout.CENTER);
        this.add(jpanelParent, BorderLayout.SOUTH);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button0){
            checkServerConnection();
        }
        if(e.getSource() == button1){
            checkServerConnection();
            SecretKey secretKey = KeyFactory.createSymmetricKey().getAESKey();
            SymmetricKey symmetricKey = KeyFactory.createSymmetricKey();
            try{
                SymmetricKey existingSymmetricKey = KeyWallet.getMainSymmetricKey();
                KeyWallet.saveSymmetricKey(existingSymmetricKey);
            }catch(NoKeyException error){
                error.printStackTrace();
                KeyWallet.saveMainSymmetricKey(symmetricKey);
            }
            textArea2.setText(secretKey.getEncoded().toString());
        }else if(e.getSource() == button2){
            checkServerConnection();
            ASymmetricKey aSymmetricKey = KeyFactory.createAsymmetricKey();

            StringBuffer stringBufferOfPublicKey = new StringBuffer();
            stringBufferOfPublicKey.append("-----BEGIN PUBLIC KEY-----\n");
            stringBufferOfPublicKey.append(aSymmetricKey.getPublicKey());
            stringBufferOfPublicKey.append("-----END PUBLIC KEY-----\n");

            StringBuffer stringBufferOfPrivateKey = new StringBuffer();
            stringBufferOfPrivateKey.append("-----BEGIN PRIVATE KEY-----\n");
            stringBufferOfPrivateKey.append(aSymmetricKey.getPrivateKey());
            stringBufferOfPrivateKey.append("-----END PRIVATE KEY-----\n");

            textArea2.setText(stringBufferOfPublicKey.append(stringBufferOfPrivateKey).toString());
        }else if(e.getSource() == button3){
            checkServerConnection();
            String filePath = textArea1.getText();
            String cipherText = null;
            if(filePath.equals("file path.")){
                new AlertGui("File path is required!", false);
            }else{
                cipherText = AESFileTranslator.image2CipherText(filePath);

                try {
                    HashMap<String, String> sendData = new HashMap<>();
                    ASymmetricKey aSymmetricKey = KeyWallet.getMainASymmetricKey();
                    sendData.put(aSymmetricKey.getPublicKey(), cipherText);
                    HTTPConnection.httpPostRequest(Config.ORIGINAL_SERVER_URL + "/test1", sendData);
                }catch(NoServerException error){
                    new AlertGui("Server is not running! due to:" + error.getMessage(), false);
                    error.printStackTrace();
                }
            }
        }else if(e.getSource() == button4){
            checkServerConnection();
            String result = HTTPConnection.httpGetRequest(
                    Config.ORIGINAL_SERVER_URL + "/test-get/", KeyWallet.getMainASymmetricKey().getPublicKey());
            textArea2.setText(result);
        }
    }

    public boolean checkServerConnection(){
        if(HTTPConnection.checkServerLive()){
            button1.setEnabled(true);
            button2.setEnabled(true);
            button3.setEnabled(true);
            button4.setEnabled(true);
            return true;
        }else{
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            new AlertGui("Server is not running!", false);
            return false;
        }
    }
}
