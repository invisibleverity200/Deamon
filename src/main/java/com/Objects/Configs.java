package com.Objects;

import java.util.ArrayList;

public interface Configs {
    ArrayList<String[]> getUpLinkChannels();

    void updateConfig();

    ArrayList<String[]> getDownLinkChannels();
}
