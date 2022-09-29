package GUIComponents;

import ClientCustomException.NoKeyException;
import ClientCustomException.NoServerException;
import FileController.AESCipherMaker;
import FileController.AESKeyMaker;
import Key.AsymmetricKeyGenerator;
import Server.Connection;
import Wallet.ASymmetricKey;
import Wallet.KeyWallet;
import Wallet.SymmetricKey;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class MyFrame extends JFrame implements ActionListener {

        /*
            <필요한 기능>
            1. create symmetric key
            2. create asymmetric key
            3. send to server
                -> choose image (filepath 입력)
                (send 버튼)
            4. receive from server
                -> choose public key (지갑에서 원하는 public key 선택)
                (receive 버튼)
         */

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
    JPanel panel1 = null;
    JPanel panel2 = null;

    public MyFrame(){
        setTitle("ImageGhostClient");
        setSize(800, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.DARK_GRAY);
        panel1 = new JPanel();
        panel2 = new JPanel();

        button1 = new JButton("Create Symmetric key");
        button2 = new JButton("Create ASymmetric key");
        button3 = new JButton("Send to server");
        button4 = new JButton("Receive from server");

        button1.setBackground(Color.GRAY);
        button2.setBackground(Color.GRAY);
        button3.setBackground(Color.GRAY);
        button4.setBackground(Color.GRAY);

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        textField1 = new JTextField();
        textArea1 = new JTextArea("Type file path here.", 10, 20);
        textArea2 = new JTextArea("Result status,", 10, 20);

        panel1.add(button1);
        panel1.add(button2);
        panel1.add(button3);
        panel1.add(button4);
        panel1.setBackground(Color.DARK_GRAY);

        panel2.add(textArea1);
        panel2.add(textArea2);
        panel2.setBackground(Color.DARK_GRAY);

        this.add(panel1, BorderLayout.NORTH);
        this.add(panel2, BorderLayout.CENTER);

        setVisible(true);
        setLocationRelativeTo(null); // 실행시 window 가운데에 위치
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button1){
            SecretKey secretKey = AESKeyMaker.generateAESKey(); // symmetric key 생성
            SymmetricKey symmetricKey = new SymmetricKey(secretKey, "new AES key");
            try{
                SymmetricKey symmetricKey1 = KeyWallet.getMainKeyForSymmetricKey(); // 메인 키를 불러옴.
                KeyWallet.saveKeyForSymmetricKey(symmetricKey); // 일반 키로 저장
            }catch(NoKeyException error){ // Main AES Key가 없을 경우 NoKeyException 발생
                error.printStackTrace();
                KeyWallet.saveKeyAsMainKeyForSymmetricKey(symmetricKey); // 메인 키로 저장
            }
            textArea2.setText(secretKey.getEncoded().toString()); // 화면에 출력
        }else if(e.getSource() == button2){
            // asymmetric key pair 생성
            AsymmetricKeyGenerator asymmetricKeyGenerator = new AsymmetricKeyGenerator();
            List<String> keyList = asymmetricKeyGenerator.generateKeyPair(); // 비대칭키 생성
            textArea2.setText(keyList.get(0) + keyList.get(1)); // 화면에 출력
        }else if(e.getSource() == button3){
            // 서버로 CipherText 전송
            String filePath = textArea1.getText(); // 파일 경로를 읽어들임.
            String cipherText = null;
            if(filePath.equals("file path.")){
                // 에러 발생.
            }else{
                try {
                    cipherText = AESCipherMaker.encryptText(filePath, KeyWallet.getMainKeyForSymmetricKey().getKey()); // 선택된 파일 암호화
                }catch(Exception error){
                    error.printStackTrace();
                }
                try {
                    HashMap<String, String> sendData = new HashMap<>();
                    ASymmetricKey aSymmetricKey = KeyWallet.getMainKeyForASymmetricKey(); // 식별자로 사용할 Main 비대칭키 불러오기
                    sendData.put(aSymmetricKey.getPublicKey(), cipherText); // Main 비대칭키의 public Key가 서버의 사용자 식별자
                    Connection.httpRequestPost("http://localhost:8080/test1", sendData); // Server에 Post 요청

                }catch(NoServerException error){
                    error.printStackTrace();
                }
            }
        }else if(e.getSource() == button4){
            // 서버에서 CipherText 받아오기
            HashMap<String, String> result = Connection.httpRequestGet("http://localhost:8080/test1", KeyWallet.getMainKeyForASymmetricKey().getPublicKey());

            // textArea2 에 결과 출력

        }
    }
}
