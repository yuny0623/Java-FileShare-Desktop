package org.imageghost.GUIComponents;

import org.imageghost.OpenChat.ServerAction;
import org.imageghost.Utils.CustomOutputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class ServerGui extends JFrame implements ActionListener{
    Container container = getContentPane();
    JTextArea jTextArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(jTextArea);
    JButton serverStartButton = new JButton("Start Open Chat Server");

    public ServerGui(){
        setTitle("ServerLogGui");
        setSize(300, 300);
        setLocation(400, 400);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        container.setLayout(new BorderLayout());

        container.add("Center", scrollPane);
        container.add("South", serverStartButton);

        serverStartButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == serverStartButton) {
            serverStartButton.setEnabled(false);
//            PrintStream printStream = null;
//            try {
//                printStream = new PrintStream(new CustomOutputStream(jTextArea), true, "UTF-8");
//            } catch (UnsupportedEncodingException ex) {
//                throw new RuntimeException(ex);
//            }
//            System.setOut(printStream);
//            System.setErr(printStream);

            Thread thread = new Thread(new ServerAction());
            thread.start();
        }
    }
}
