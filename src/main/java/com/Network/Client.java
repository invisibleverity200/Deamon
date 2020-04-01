package com.Network;

import com.Objects.Discrete;

public interface Client extends Runnable {
    boolean sendDiscrete(Discrete discrete);

    void closeConnections();//in order to have a quit button you need this function!
}
