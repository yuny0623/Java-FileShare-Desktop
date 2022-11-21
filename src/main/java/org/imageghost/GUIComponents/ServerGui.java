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
    JButton serverStopButton = new JButton("Stop Open Chat Server");

    public ServerGui(){
        setTitle("ServerGui");
        setSize(300, 600);
        setLocation(100, 100);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jTextArea.setEditable(false);
        container.setLayout(new BorderLayout());
        container.add("Center", scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 0));
        buttonPanel.add(serverStartButton);
        buttonPanel.add(serverStopButton);
        container.add("South", buttonPanel);

        serverStartButton.addActionListener(this);
        serverStopButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == serverStartButton) {
            serverStartButton.setEnabled(false);
            PrintStream printStream = new PrintStream(new CustomOutputStream(jTextArea));
            System.setOut(printStream);
            System.setErr(printStream);
            System.out.println("Server Thread Starting...");

            Thread thread = new Thread(new ServerAction());
            thread.start();
        }else if(e.getSource() == serverStopButton){
            System.exit(0);
        }
    }
}
