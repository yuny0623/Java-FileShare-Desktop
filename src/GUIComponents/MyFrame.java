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

    public MyFrame(){
        setTitle("ImageGhostClient");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(new FlowLayout());


        button1 = new JButton("Create Symmetric Key");
        button2 = new JButton("Create ASymmetric Key");
        button3 = new JButton("Send");
        button4 = new JButton("Receive");

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        c.add(button1);
        c.add(button2);
        c.add(button3);
        c.add(button4);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button1){
            SecretKey secretKey = AESKeyMaker.generateAESKey();
            System.out.println(secretKey.toString());
        }else if(e.getSource() == button2){

        }else if(e.getSource() == button3){

        }
    }
}
