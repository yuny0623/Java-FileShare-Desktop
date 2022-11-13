package org.imageghost.GUIComponents;

import org.imageghost.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class FixedMainGui extends JFrame implements ActionListener {

    Container container = getContentPane();
    JButton clientButton = new JButton("Client");
    JButton serverButton = new JButton("Server");
    JPanel buttonPanel = new JPanel();

    JTextArea jTextArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(jTextArea);

    public FixedMainGui(){
        setTitle("ImageGhost");
        setSize(550, 400);
        setLocation(400, 400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        container.setLayout(new BorderLayout());

        buttonPanel.add("East", clientButton);
        buttonPanel.add("West", serverButton);
        container.add("Center", scrollPane);
        container.add("South", buttonPanel);

        clientButton.addActionListener(this);
        serverButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == clientButton){
            try {
                InetAddress ia = InetAddress.getLocalHost();
                String ipStr = ia.toString();
                // ia.toString() -> "DESKTOP-5QRSVM2/192.168.70.1"
                String ip = ipStr.substring(ipStr.indexOf("/") + 1);
                new ClientGui(ip, Config.TCP_IP_CONNECTION_DEFAULT_PORT);
            }catch(Exception err){
                err.printStackTrace();
            }
        }
        else if(e.getSource() == serverButton){
            new ServerGui();
        }
    }
}
