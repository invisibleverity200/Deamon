package com.Objects;

import java.util.ArrayList;

public interface Configs {
    ArrayList<Discrete> getUpLinkChannels();

    void updateConfig();

    ArrayList<Discrete> getDownLinkChannels();
}
