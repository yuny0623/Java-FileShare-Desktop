package org.imageghost.GUIComponents;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class CustomOutputStream extends OutputStream {

    private JTextArea jTextArea;

    public CustomOutputStream(JTextArea jTextArea){
        this.jTextArea = jTextArea;
    }

    @Override
    public void write(int b) throws IOException {
        jTextArea.append(String.valueOf((char)b));
        jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
    }
}
