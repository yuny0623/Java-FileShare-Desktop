package org.imageghost.GUIComponents;

import javax.swing.*;
public class UserInfoResponseGui extends JFrame {
    public UserInfoResponseGui(String info){
        setTitle("userInfoResponse");
        setBounds(0, 0, 600, 300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea(info);
        JScrollPane jScrollPane = new JScrollPane(textArea);
        this.add(jScrollPane);
    }
}
