package com.Network;

import com.Objects.Config;
import com.Objects.Configs;
import com.Objects.Discrete;

import javax.swing.*;
import java.io.IOException;

public class DaemonClient implements Client {
    private JButton[] upLinkButtons;
    private JButton[] downLinkButtons;
    private ReceiveSocket receiveSocket = null;
    private SendSocket sendSocket = null;
    private Configs config;

    private final String IP_ADDR = "192.168.1.204";
    private final int RECEIVE_PORT = 31242;
    private final int SEND_PORT = 1272;


    //[inOrOut,idx,flag]
    public DaemonClient(JButton[] upLinkButtons, JButton[] downLinkButtons, Configs config) {
        this.upLinkButtons = upLinkButtons;
        this.downLinkButtons = downLinkButtons;
        this.config = config;
    }

    @Override
    public void run() {
        if (receiveSocket != null) {
            try {
                if (receiveSocket.init()) {
                    receiveSocket.receive(upLinkButtons, downLinkButtons, config.getDownLinkPosArray(), config.getUpLinkPosArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean init() {
        try {
            receiveSocket = new ReceiveSocket(IP_ADDR, RECEIVE_PORT);
            sendSocket = new SendSocket(IP_ADDR, SEND_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendDiscrete(Discrete discrete) {
        sendSocket.send(discrete);
        return true;
    }

    public void closeConnections() {
        try {
            sendSocket.closeConnection();
            receiveSocket.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
