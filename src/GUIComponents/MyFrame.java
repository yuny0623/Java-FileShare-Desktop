package GUIComponents;

import FileController.AESKeyMaker;
import Key.AsymmetricKeyGenerator;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame implements ActionListener {
    JButton button1 = null;
    JButton button2 = null;
    JButton button3 = null;
    JButton button4 = null;

    JTextField textField1 = null;
    JTextField textField2 = null;
    JTextField textField3 = null;
    JTextField textField4 = null;

    public MyFrame(){
        setTitle("ImageGhostClient");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.DARK_GRAY);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());

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

        c.add(button1);
        textField1 = new JTextField();
        c.add(textField1);

        c.add(button2);
        textField2 = new JTextField();
        c.add(textField1);

        c.add(button3);
        textField3 = new JTextField();
        c.add(textField1);

        c.add(button4);
        textField4 = new JTextField();
        c.add(textField1);

        setVisible(true);

        setLocationRelativeTo(null); // 실행시 window 가운데에 위치
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button1){
            // symmetric key 생성
            SecretKey secretKey = AESKeyMaker.generateAESKey();
            System.out.println(secretKey.getEncoded());

        }else if(e.getSource() == button2){
            // asymmetric key pair 생성

        }else if(e.getSource() == button3){
            // 서버로 이미지 전송

        }else if(e.getSource() == button4){
            // 서버에서 이미지 받아오기

        }
    }
}
