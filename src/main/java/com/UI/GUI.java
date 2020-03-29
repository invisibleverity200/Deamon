package com.UI;

import com.Objects.Config;
import com.Objects.Configs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class GUI extends JFrame {
    int nameType = 1;

    public GUI() {
        final AtomicReferenceArray<JButton>[] buttons = new AtomicReferenceArray[]{new AtomicReferenceArray<>(new JButton[0])};
        Configs config = new Config();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 10));

        JMenuBar menuBar = new JMenuBar();

        JMenu channelNameTypes = new JMenu("Channel Names");

        menuBar.add(channelNameTypes);

        JMenuItem upLinkWindow = new JMenuItem("Up Link");
        JMenuItem downLinkWindow = new JMenuItem("Down Link");

        upLinkWindow.addActionListener((ActionEvent e) -> {
            buttons[0] = new AtomicReferenceArray<>(setUpContentPane(contentPanel, config.getUpLinkChannels()));
            setSize(config.getUpLinkChannels().size() * 150, config.getUpLinkChannels().size() / 10 * 20 + 100);
        });
        downLinkWindow.addActionListener((ActionEvent e) -> {
            buttons[0] = new AtomicReferenceArray<>(setUpContentPane(contentPanel, config.getDownLinkChannels()));
            setSize(config.getDownLinkChannels().size() * 150, config.getDownLinkChannels().size() / 10 * 20 + 100);
        });

        channelNameTypes.add(upLinkWindow);
        channelNameTypes.add(downLinkWindow);

        buttons[0] = new AtomicReferenceArray<>(setUpContentPane(contentPanel, config.getUpLinkChannels()));

        setContentPane(contentPanel);

        setJMenuBar(menuBar);

        pack();

        setTitle("DeamonGUI");

        setVisible(true);

        setSize(config.getUpLinkChannels().size() * 150, config.getUpLinkChannels().size() / 10 * 20 + 100);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private JButton[] setUpContentPane(JPanel contentPane, ArrayList<String[]> channels) {
        contentPane.removeAll();
        if (channels.size() >= 10) {
            contentPane.setLayout(new GridLayout(0, 10));
        }else {
            contentPane.setLayout(new GridLayout(0, channels.size()));
        }
        JButton[] buttons = new JButton[channels.size()];
        int index = 0;
        for (String[] channel : channels) {
            buttons[index] = new JButton(channel[nameType].substring(1, channel[nameType].length() - 1));
            buttons[index].addActionListener((ActionEvent e) -> {
                System.out.println("test");
                //TODO write methode
            });
            contentPane.add(buttons[index]);
            index++;
        }
        revalidate();
        pack();
        repaint();

        return buttons;
    }
}
