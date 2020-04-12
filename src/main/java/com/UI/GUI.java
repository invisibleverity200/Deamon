package com.UI;

import com.Network.Client;
import com.Network.DaemonClient;
import com.Objects.Config;
import com.Objects.Configs;
import com.Objects.Discrete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GUI extends JFrame {
    private int nameType = 1;
    private Configs config;
    private Client daemonClient;
    private Thread clientThread;
    private JLabel statusLabel;
    private final JButton[][] buttonsAstsToCids = {null};
    private final JButton[][] buttonsCidsToAsts = {null};

    public GUI() {
        config = new Config();
        AtomicInteger currentSide = new AtomicInteger();

        buttonsAstsToCids[0] = getAstsToCids(config.getAstsToCidsChannels());
        buttonsCidsToAsts[0] = getCidsToAsts(config.getCidsToAstsChannels());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 10));

        JMenuBar menuBar = new JMenuBar();

        JMenu channelNameTypes = new JMenu("Sides");

        menuBar.add(channelNameTypes);

        JMenuItem AstsToCidsWindow = new JMenuItem("AstsToCids");
        JMenuItem CidsToAstsWindow = new JMenuItem("CidsToAsts");

        AstsToCidsWindow.addActionListener((ActionEvent e) -> {
            currentSide.set(0);
            if (buttonsAstsToCids[0] == null) {
                buttonsAstsToCids[0] = getAstsToCids(config.getAstsToCidsChannels());
                setUpContentPane(buttonsAstsToCids[0], contentPanel);
            } else {
                setUpContentPane(buttonsAstsToCids[0], contentPanel);
            }
        });
        CidsToAstsWindow.addActionListener((ActionEvent e) -> {
            currentSide.set(1);
            if (buttonsCidsToAsts[0] == null) {
                buttonsCidsToAsts[0] = getCidsToAsts(config.getCidsToAstsChannels());
                setUpContentPane(buttonsCidsToAsts[0], contentPanel);
            } else {
                setUpContentPane(buttonsCidsToAsts[0], contentPanel);
            }
        });

        JMenu operations = new JMenu("Operations");
        JMenu nameSets = new JMenu("Name Sets");

        JMenuItem nameSet1 = new JMenuItem("Name set 1"); //i don´t like  this layout thb
        nameSet1.addActionListener((ActionEvent e) -> changeNameSet(
                currentSide.get() == 0 ? buttonsAstsToCids[0] : buttonsCidsToAsts[0],
                0,
                currentSide.get()
                )
        );

        JMenuItem nameSet2 = new JMenuItem("Name set 2");
        nameSet2.addActionListener((ActionEvent e) -> changeNameSet(currentSide.get() == 0 ? buttonsAstsToCids[0] : buttonsCidsToAsts[0], 1, currentSide.get()));

        JMenuItem nameSet3 = new JMenuItem("Name set 3");
        nameSet3.addActionListener((ActionEvent e) -> changeNameSet(currentSide.get() == 0 ? buttonsAstsToCids[0] : buttonsCidsToAsts[0], 2, currentSide.get()));

        JMenuItem reconnect = new JMenuItem("Reconnect");
        reconnect.addActionListener((ActionEvent e) -> {
            if (daemonClient != null) daemonClient.closeConnections();
            if (clientThread != null) clientThread.stop();
            while (createClient(buttonsAstsToCids, buttonsCidsToAsts, false, statusLabel)) ;
        });

        JMenuItem reload = new JMenuItem("Reload");
        reload.addActionListener((ActionEvent e) -> {
            config.updateConfig();
            buttonsAstsToCids[0] = getAstsToCids(config.getAstsToCidsChannels());
            buttonsCidsToAsts[0] = getCidsToAsts(config.getCidsToAstsChannels());
            setUpContentPane(currentSide.get() == 0 ? buttonsAstsToCids[0] : buttonsCidsToAsts[0], contentPanel);
        });

        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener((ActionEvent e) -> {
            if (daemonClient != null) daemonClient.closeConnections();
            if (clientThread != null) clientThread.stop();
            System.exit(1);
        });

        nameSets.add(nameSet1);
        nameSets.add(nameSet2);
        nameSets.add(nameSet3);

        operations.add(reconnect);
        operations.add(reload);
        operations.add(quit);

        statusLabel = new JLabel("Disconnected");
        statusLabel.setForeground(Color.RED);

        menuBar.add(nameSets);
        menuBar.add(operations);
        menuBar.add(statusLabel);

        channelNameTypes.add(AstsToCidsWindow);
        channelNameTypes.add(CidsToAstsWindow);

        setContentPane(contentPanel);

        setJMenuBar(menuBar);

        pack();

        setTitle("DaemonGUI");

        setVisible(true);

        setUpContentPane(buttonsAstsToCids[0], contentPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        while (createClient(buttonsAstsToCids, buttonsCidsToAsts, false, statusLabel)) ;

    }

    private boolean createClient(JButton[][] buttonsUpLink, JButton[][] buttonsDownLink, boolean failed, JLabel statusLabel) {
        try {
            daemonClient = new DaemonClient(buttonsUpLink[0], buttonsDownLink[0], config, statusLabel, config.getIp());
        } catch (IOException e) {
            int returnVal = JOptionPane.showConfirmDialog(null,
                    "Cant Connect to Server pls try again\nretry?", "An Error occurred",
                    JOptionPane.YES_NO_OPTION);
            statusLabel.setText("Disconnected");
            statusLabel.setForeground(Color.red);
            if (returnVal == JOptionPane.YES_OPTION) {
                return true;
            } else {
                if (daemonClient != null) daemonClient.closeConnections();
                System.exit(1);
            }
        }
        if (!failed) {//FIXME possible bug!!!
            clientThread = new Thread(daemonClient);
            clientThread.start();
            return false;
        }
        return false;
    }

    private JButton[] getAstsToCids(ArrayList<Discrete> discretes) {
        int i = 0;
        JButton[] buttons = new JButton[discretes.size()];
        int index = 0;
        for (Discrete discrete : discretes) {
            buttons[index] = new JButton(discrete.getAttributes()[nameType].substring(1, discrete.getAttributes()[nameType].length() - 1));
            if (discrete.getAttributes()[discrete.getAttributes().length - 1].substring(1, discrete.getAttributes().length).equals("true")) {
                buttons[index].setBackground(Color.ORANGE);
            } else {
                buttons[index].setBackground(Color.RED);
            }
            buttons[index].setPreferredSize(new Dimension(150, 40));
            int finalI = i;
            buttons[index].addActionListener((ActionEvent e) -> {
                int x = finalI;
                if (config.getAstsToCidsChannels().get(x).getFlag()) {
                    config.getAstsToCidsChannels().get(x).setFlag(false);
                } else {
                    config.getAstsToCidsChannels().get(x).setFlag(true);
                }
                Discrete thisButton = config.getAstsToCidsChannels().get(x);
                if (thisButton.getFlag()) {
                    thisButton.setFlag(false);
                } else {
                    thisButton.setFlag(true);
                }
                if (daemonClient != null) {
                    if (!daemonClient.sendDiscrete(thisButton)) {
                        statusLabel.setText("Disconnected");
                        statusLabel.setForeground(Color.red);
                        daemonClient.closeConnections();
                    }
                }
            });
            i++;
            index++;
        }

        return buttons;
    }

    private JButton[] getCidsToAsts(ArrayList<Discrete> discretes) {
        JButton[] buttons = new JButton[discretes.size()];
        int index = 0;
        for (Discrete discrete : discretes) {
            buttons[index] = new JButton(discrete.getAttributes()[nameType].substring(1, discrete.getAttributes()[nameType].length() - 1));
            if (discrete.getAttributes()[discrete.getAttributes().length - 1].substring(1, discrete.getAttributes().length).equals("true")) {
                buttons[index].setBackground(Color.ORANGE);
            } else {
                buttons[index].setBackground(Color.RED);
            }
            index++;
        }
        return buttons;
    }

    private void setUpContentPane(JButton[] buttons, JPanel contentPane) {
        contentPane.removeAll();
        if (buttons.length >= 10) {
            contentPane.setLayout(new GridLayout(0, 10));
        } else {
            contentPane.setLayout(new GridLayout(0, buttons.length));
        }
        for (JButton button : buttons) {
            contentPane.add(button);
        }
        if (buttons.length * 300 >= 650) {
            setSize(buttons.length * 150, buttons.length / 10 * 20 + 100);
        } else {
            setSize(650, buttons.length / 10 * 20 + 100);
        }
        revalidate();
        pack();
        repaint();
    }

    private void changeNameSet(JButton[] buttons, int nameSetId, int sideIdx) {
        if (sideIdx == 0) {
            for (int index = 0; index < buttons.length; index++) {
                buttons[index].setText(config.getAstsToCidsChannels().get(index).getAttributes()[nameSetId + 1].substring(1, config.getAstsToCidsChannels().get(index).getAttributes()[nameSetId + 1].length() - 1));
            }
        } else {
            for (int index = 0; index < buttons.length; index++) {
                buttons[index].setText(config.getCidsToAstsChannels().get(index).getAttributes()[nameSetId + 1].substring(1, config.getCidsToAstsChannels().get(index).getAttributes()[nameSetId + 1].length() - 1));
            }
        }
    }
}
