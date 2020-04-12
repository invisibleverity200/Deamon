package com.Network;

import com.Objects.Discrete;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

class ReceiveSocket extends Socket {
    private DataOutputStream outputStream;
    private DataInputStream dataInputStream;

    //[inOrOut,idx,flag]
    ReceiveSocket(String ipAddr, int port) throws IOException {
        super(ipAddr, port);
        init();
    }

    private void init() throws IOException {
        try {
            outputStream = new DataOutputStream(this.getOutputStream());
            dataInputStream = new DataInputStream(this.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    void closeConnection() throws IOException {
        if (outputStream != null) outputStream.close();
        if (dataInputStream != null) dataInputStream.close();
    }

    //[inOrOut,idx,flag]
    boolean receive(JButton[] astsToCidsButtons, JButton[] cidsToAstsButtons, int[] posArrayCidsToAsts, int[] posArrayAstsToCids, ArrayList<Discrete> discretes){
        long tempTime = System.currentTimeMillis() + 1000;
        try {
            outputStream.write(1);


            while (true) { //TODO write method
                if (dataInputStream.available() >= (Integer.BYTES * 3)) {//FIXME possible bug
                    int SideIdx = dataInputStream.readInt();
                    if (SideIdx == 1) {
                        int idx = dataInputStream.readInt();
                        int flag = dataInputStream.readInt();
                        if (flag == 1) {
                            int tempIdx = posArrayCidsToAsts[idx];
                            if (tempIdx != -1) cidsToAstsButtons[tempIdx].setBackground(Color.GREEN);
                        } else {
                            int tempIdx = posArrayCidsToAsts[idx];
                            if (tempIdx != -1) cidsToAstsButtons[tempIdx].setBackground(Color.orange);
                        }
                    } else if (SideIdx == 0) {
                        int idx = dataInputStream.readInt();
                        int flag = dataInputStream.readInt();
                        if (flag == 1) {
                            int tempIdx = posArrayAstsToCids[idx];
                            if (tempIdx != -1) {
                                astsToCidsButtons[tempIdx].setBackground(Color.GREEN);
                                discretes.get(tempIdx).setFlag(true);
                            }
                        } else {
                            int tempIdx = posArrayAstsToCids[idx];
                            if (tempIdx != -1) {
                                astsToCidsButtons[tempIdx].setBackground(Color.orange);
                                discretes.get(tempIdx).setFlag(false);
                            }
                        }
                    } else {
                        tempTime = System.currentTimeMillis();
                        dataInputStream.readInt();
                        dataInputStream.readInt();
                    }
                }
                if (System.currentTimeMillis() - tempTime > 4000) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
