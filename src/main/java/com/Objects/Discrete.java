package com.Objects;

public class Discrete {
    private String[] attributes;
    private boolean flag;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    Discrete(String[] attributes) {
        this.attributes = attributes;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public int getDiscreteIdx() {
        //TODO write methode!
        return 1;
    }
}
