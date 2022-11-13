package org.imageghost.GUIComponents;

import org.imageghost.OpenChat.ServerAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGui extends JFrame implements ActionListener{
    Container container = getContentPane();
    JTextArea jTextArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(jTextArea);
    JButton serverStartButton = new JButton("Start Open Chat Server");

    public ServerGui(){
        setTitle("ServerLogGui");
        setSize(550, 400);
        setLocation(400, 400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        container.setLayout(new BorderLayout());

        container.add("Center", scrollPane);
        container.add("South", serverStartButton);

        serverStartButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == serverStartButton) {
            Thread thread = new Thread(new ServerAction());
            thread.start();
        }
    }
}
