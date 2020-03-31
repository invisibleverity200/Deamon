package com.Objects;

public class Discrete {
    private String[] attributes;
    private boolean flag;

    private final int NUM_ASTS2SUT_PORTS = 1;
    private final int PORTSIZE = 1;

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
        //disc_idx = card * NUM_ASTS2SUT_PORTS * PORTSIZE +
        //	  port * PORTSIZE + bit_no;
        int card_bitidx = Integer.parseInt("" + attributes[0].charAt(3) + attributes[0].charAt(4), 16);
        return (attributes[0].charAt(1) - '0') * NUM_ASTS2SUT_PORTS * PORTSIZE + (attributes[0].charAt(2) - 'A') * PORTSIZE + card_bitidx;
    }
}//0B0C
