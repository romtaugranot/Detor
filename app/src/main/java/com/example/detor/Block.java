package com.example.detor;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Block {

    private com.google.android.gms.maps.model.LatLng pos;

    private List<Locker> lockers;

    private int num;

    private boolean isFull;

    public Block(int num, LatLng pos) {
        this.pos = pos;
        this.lockers = new ArrayList<>();
        this.num = num;
    }

    public LatLng getPos() {
        return pos;
    }

    public void setPos(LatLng pos) {
        this.pos = pos;
    }

    public List<Locker> getLockers() {
        return lockers;
    }

    public void addLocker(Locker locker){
        lockers.add(locker);
    }

    public void setFull(boolean isFull){
        this.isFull= isFull;
    }

    public boolean isFull() {return isFull;}

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
