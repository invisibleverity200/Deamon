package com.Network;

import com.Objects.Configs;
import com.Objects.Discrete;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DaemonClient implements Client {
    private JButton[] astsToCidsButtons;
    private JButton[] cidsToAstsButtons;
    private ReceiveSocket receiveSocket;
    private SendSocket sendSocket;
    private Configs config;
    private JLabel statusLabel;
    private String ip;

    private static int RECEIVE_PORT = 2342;
    private static int SEND_PORT = 4233;


    //[inOrOut,idx,flag]
    public DaemonClient(JButton[] astsToCidsButtons, JButton[] cidsToAstsButtons, Configs config, JLabel statusLabel, String ip) throws IOException {
        this.astsToCidsButtons = astsToCidsButtons;
        this.cidsToAstsButtons = cidsToAstsButtons;
        this.config = config;
        this.ip = ip;

        receiveSocket = new ReceiveSocket(this.ip, RECEIVE_PORT);
        sendSocket = new SendSocket(this.ip, SEND_PORT);

        this.statusLabel = statusLabel;

        statusLabel.setText("Connected");
        statusLabel.setForeground(Color.GREEN);

    }

    @Override
    public void run() {
        if (receiveSocket != null) {

            if (!receiveSocket.receive(astsToCidsButtons, cidsToAstsButtons, config.getCidsToAstsArray(), config.getAstsToCidsArray(), config.getAstsToCidsChannels())) {
                statusLabel.setText("Disconnected");
                statusLabel.setForeground(Color.red);
            }
            closeConnections();

        }
    }

    public boolean sendDiscrete(Discrete discrete) {

        try {
            sendSocket.send(discrete);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void closeConnections() {
        try {
            if (sendSocket != null) sendSocket.closeConnection();
            if (sendSocket != null) receiveSocket.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}