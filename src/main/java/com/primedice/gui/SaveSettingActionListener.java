package com.primedice.gui;

import com.primedice.client.Config;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveSettingActionListener extends App implements ActionListener {
    private String key;
    private String value;

    public SaveSettingActionListener(String key, JTextField textField) {
        super();
        this.key = key;
        this.value = textField.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Config.setValue(key, value);
        Config.writeNewConfigFile();
    }
}
