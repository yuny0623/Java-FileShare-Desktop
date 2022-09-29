package GUIComponents;

import ClientCustomException.NoKeyException;
import ClientCustomException.NoServerException;
import FileController.AESCipherMaker;
import FileController.AESFileTranslator;
import FileController.AESKeyMaker;
import Key.AsymmetricKeyGenerator;
import Server.Connection;
import Wallet.KeyWallet;
import Wallet.SymmetricKey;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MyFrame extends JFrame implements ActionListener {
    JButton button1 = null;
    JButton button2 = null;
    JButton button3 = null;
    JButton button4 = null;

    JTextField textField1 = null;
    JTextField textField2 = null;
    JTextField textField3 = null;
    JTextField textField4 = null;

    JTextArea textArea1 = null;

    JPanel panel1 = null;
    JPanel panel2 = null;

    public MyFrame(){
        setTitle("ImageGhostClient");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.DARK_GRAY);

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
        textArea1 = new JTextArea("file path.", 10, 20);

        panel1.add(button1);
        panel1.add(button2);
        panel1.add(button3);
        panel1.add(button4);
        panel1.setBackground(Color.DARK_GRAY);

        panel2.add(textArea1);
        panel2.setBackground(Color.DARK_GRAY);

        this.add(panel1, BorderLayout.NORTH);
        this.add(panel2, BorderLayout.SOUTH);

        setVisible(true);
        setLocationRelativeTo(null); // 실행시 window 가운데에 위치
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button1){
            SecretKey secretKey = AESKeyMaker.generateAESKey(); // symmetric key 생성
            SymmetricKey symmetricKey = new SymmetricKey(secretKey, "new AES key");
            try{
                SymmetricKey symmetricKey1 = KeyWallet.getMainKey(); // 메인 키를 불러옴.
                KeyWallet.saveKey(symmetricKey); // 일반 키로 저장
            }catch(NoKeyException error){ // Main AES Key가 없을 경우 NoKeyException 발생
                error.printStackTrace();
                KeyWallet.saveKeyAsMainKey(symmetricKey); // 메인 키로 저장
            }
            System.out.println(secretKey.getEncoded());
        }else if(e.getSource() == button2){
            // asymmetric key pair 생성
            AsymmetricKeyGenerator asymmetricKeyGenerator = new AsymmetricKeyGenerator();
            HashMap<String, String> keyPair = asymmetricKeyGenerator.generateKeyPair();

            System.out.println(keyPair.toString());

        }else if(e.getSource() == button3){
            // 서버로 CipherText 전송
            String filePath = textArea1.getText();
            if(filePath.equals("file path.")){
                // 에러 발생.
            }else{
                try {
                    String cipherText = AESCipherMaker.encryptText(filePath, KeyWallet.getMainKey().getKey());
                }catch(Exception error){
                    error.printStackTrace();
                }
                try {
                    // Connection.httpRequestPost(); // Server에 Post 요청
                }catch(NoServerException error){
                    error.printStackTrace();
                }
            }
        }else if(e.getSource() == button4){
            // 서버에서 CipherText 받아오기

        }
    }
}
