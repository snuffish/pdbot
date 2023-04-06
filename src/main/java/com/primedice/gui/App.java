package com.primedice.gui;

import com.primedice.client.Config;
import org.jgraph.JGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class App extends JFrame {
    private static JFrame frame;
    private static JPanel bettingPanel;
    private static JPanel settingsPanel;
    private static JPanel scriptPanel;
    private static JTabbedPane tabbedPane;
    private static HashMap<JLabel, JTextField> configData;

    private static JButton betButton;
    private static Boolean isBetting;
    private static Thread bettingThread;

    public static JTextArea textArea;

    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private static void setup() {
        Config.init("settings.conf");

        frame = new JFrame("PrimeDiceBOT (GUI)");
        frame.setLayout(new BorderLayout());
        frame.setSize(1000, 800);

        tabbedPane = new JTabbedPane();
        bettingPanel = new JPanel();
        tabbedPane.addTab("Betting", bettingPanel);
        settingsPanel = new JPanel();
        tabbedPane.addTab("Settings", settingsPanel);
        scriptPanel = new JPanel();
        tabbedPane.addTab("Scripting", scriptPanel);

        isBetting = false;
        betButton = new JButton("Start betting");
        textArea = new JTextArea();

        bettingThread = new Thread(new BettingThread());
    }

    public static void main(String[] args) {
        setup();

        printBetting();
        printSettings();

        frame.add(tabbedPane);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void printBetting() {
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isBetting) {
                    isBetting = true;
                    betButton.setText("Start betting");
                    bettingThread.start();
                } else {
                    isBetting = false;
                    betButton.setText("Stop betting");
                    bettingThread.interrupt();
                }
            }
        });
        bettingPanel.add(betButton);

        // Betting overview

        bettingPanel.add(textArea);
    }

    private static void printSettings() {
        configData = new HashMap<JLabel, JTextField>();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key;
                String value;

                Set set = configData.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry)iterator.next();
                    JLabel label = (JLabel) mentry.getKey();
                    JTextField textField = (JTextField) mentry.getValue();
                    key = label.getText();
                    value = textField.getText();

                    Config.setValue(key, value);
                }

                Config.writeNewConfigFile();

                showMessage("Settings successfully saved!");
            }
        });

        settingsPanel.add(saveButton);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(Config.getAllConfigValues().size(), 2));

        for (int i = 0; i < Config.getAllConfigValues().size(); i++) {
            String key = Config.getAllConfigValues().get(i).getKey();
            String value = Config.getAllConfigValues().get(i).getValue();

            JLabel keyLabel = new JLabel(key);
            try {
                if (value != null) { }
            } catch (NullPointerException e) {
                keyLabel.setForeground(Color.RED);
            }

            settingsPanel.add(keyLabel);
            final JTextField textField = new JTextField(value);
            textField.setSize(new Dimension(10, 10));
            settingsPanel.add(textField);

            configData.put(keyLabel, textField);
        }

        App.settingsPanel.add(settingsPanel);
    }
}
