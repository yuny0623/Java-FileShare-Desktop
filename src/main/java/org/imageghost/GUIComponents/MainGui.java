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

        this.add(panel0, BorderLayout.NORTH);   // server status 표시창
        this.add(panel1, BorderLayout.CENTER);  // 버튼 패널 표시창
        this.add(jpanelParent, BorderLayout.SOUTH);   // 결과 TextArea 표시창

        setVisible(true);
        setLocationRelativeTo(null); // 실행시 window 가운데에 위치
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button0){
            checkServerConnection();
        }
        if(e.getSource() == button1){ // create symmetric key
            checkServerConnection();
            SecretKey secretKey = KeyFactory.createSymmetricKey().getAESKey(); // symmetric key 생성
            SymmetricKey symmetricKey = KeyFactory.createSymmetricKey();
            try{

                SymmetricKey existingSymmetricKey = KeyWallet.getMainKeyForSymmetricKey(); // 메인 키를 불러옴.
                KeyWallet.saveKeyForSymmetricKey(existingSymmetricKey); // 일반 키로 저장
            }catch(NoKeyException error){ // Main AES Key가 없을 경우 NoKeyException 발생
                error.printStackTrace();
                KeyWallet.saveKeyAsMainKeyForSymmetricKey(symmetricKey); // 메인 키로 저장
            }
            textArea2.setText(secretKey.getEncoded().toString()); // 출력
        }else if(e.getSource() == button2){ // create asymmetric key
            checkServerConnection();
            // asymmetric key pair 생성
            ASymmetricKey aSymmetricKey = KeyFactory.createAsymmetricKey(); // 비대칭키 생성

            StringBuffer stringBufferOfPublicKey = new StringBuffer();  // public key string
            stringBufferOfPublicKey.append("-----BEGIN PUBLIC KEY-----\n");
            stringBufferOfPublicKey.append(aSymmetricKey.getPublicKey());
            stringBufferOfPublicKey.append("-----END PUBLIC KEY-----\n");

            StringBuffer stringBufferOfPrivateKey = new StringBuffer(); // private key string
            stringBufferOfPrivateKey.append("-----BEGIN PRIVATE KEY-----\n");
            stringBufferOfPrivateKey.append(aSymmetricKey.getPrivateKey());
            stringBufferOfPrivateKey.append("-----END PRIVATE KEY-----\n");

            textArea2.setText(stringBufferOfPublicKey.append(stringBufferOfPrivateKey).toString()); // 출력
        }else if(e.getSource() == button3){ // send to server
            checkServerConnection();

            // 서버로 CipherText 전송
            String filePath = textArea1.getText(); // 파일 경로를 읽어들임.
            String cipherText = null;
            if(filePath.equals("file path.")){
                new AlertGui("File path is required!", false);
            }else{
                cipherText = AESFileTranslator.Image2AESCipherText(filePath);

                try {
                    HashMap<String, String> sendData = new HashMap<>();
                    ASymmetricKey aSymmetricKey = KeyWallet.getMainKeyForASymmetricKey();   // 식별자로 사용할 Main 비대칭키 불러오기
                    sendData.put(aSymmetricKey.getPublicKey(), cipherText);                 // Main 비대칭키의 public Key가 서버의 사용자 식별자
                    HTTPConnection.httpPostRequest(Config.ORIGINAL_SERVER_URL + "/test1", sendData); // Server에 Post 요청
                }catch(NoServerException error){
                    new AlertGui("Server is not running! due to:" + error.getMessage(), false);
                    error.printStackTrace();
                }
            }
        }else if(e.getSource() == button4){
            checkServerConnection();
            // 서버에서 CipherText 받아오기
            String result = HTTPConnection.httpGetRequest(
                    Config.ORIGINAL_SERVER_URL + "/test-get/", KeyWallet.getMainKeyForASymmetricKey().getPublicKey());
            // textArea2 에 결과 출력
            textArea2.setText(result);
        }
    }
    /*
        버튼 누름시 서버 상태 체크
     */
    public boolean checkServerConnection(){
        if(HTTPConnection.checkServerLive()){ // if server is running.
            button1.setEnabled(true);
            button2.setEnabled(true);
            button3.setEnabled(true);
            button4.setEnabled(true);
            return true;
        }else{ // if server is not running.
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            new AlertGui("Server is not running!", false);
            return false;
        }
    }
}
