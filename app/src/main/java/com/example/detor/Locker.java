package com.example.detor;

import java.io.Serializable;

public class Locker implements Serializable {

    private boolean isTaken;

    private int num;

    public Locker(boolean isTaken, int num) {
        this.isTaken = isTaken;
        this.num = num;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
