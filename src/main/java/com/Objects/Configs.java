package com.Objects;

import java.util.ArrayList;

public interface Configs {
    ArrayList<Discrete> getAstsToCidsChannels();

    void updateConfig();

    ArrayList<Discrete> getCidsToAstsChannels();

    int[] getCidsToAstsArray();

    int[] getAstsToCidsArray();
}
