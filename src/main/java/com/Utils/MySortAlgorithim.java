package com.Utils;

import com.Objects.Discrete;

import java.util.ArrayList;

public class MySortAlgorithim implements SortAlgorithim {
    ArrayList<Discrete> data;

    public ArrayList<Discrete> sort(ArrayList<Discrete> data) {
        this.data = data;
        int pointer = 0;
        for (int i = 0; i < data.size(); i++) {
            if (check(pointer, i)) {
                Discrete temp = data.get(i);
                data.set(i, data.get(pointer));
                data.set(pointer, temp);
                i = pointer;
                pointer++;
            }

        }
        return data;
    }

    private boolean check(int pointer, int i) {
        for (int x = pointer; x < data.size(); x++) {
            if (data.get(i).getDiscreteIdx() > data.get(x).getDiscreteIdx()) {
                return false;
            }
        }
        return true;
    }
}
