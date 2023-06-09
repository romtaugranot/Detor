package com.example.detor;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {

    double lat;

    double lng;

    private List<Locker> lockers;

    private int num;

    private boolean isFull;

    public Block(int num, LatLng pos) {
        this.lat = pos.latitude;
        this.lng = pos.longitude;
        this.lockers = new ArrayList<>();
        this.num = num;
    }

    public LatLng getPos() {
        return new LatLng(lat, lng);
    }

    public void setPos(LatLng pos) {
        this.lat = pos.latitude;
        this.lng = pos.longitude;
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

    /**
     * @pre: there's an empty locker.
     * @return
     */
    public int getIndexOfFreeLockerAndUpdateBlock() {
        for (int i = 0; i < lockers.size(); i++){
            if (!lockers.get(i).isTaken()) {
                lockers.get(i).setTaken(true);
                return i;
            }
        }
        return -1; // should never get here.
    }

    public int getIndexOfFreeLocker() {
        for (int i = 0; i < lockers.size(); i++){
            if (!lockers.get(i).isTaken()) {
                return i;
            }
        }
        return -1; // should never get here.
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int howManyAvailable() {
        int count = 0;
        for (Locker locker : lockers){
            if (!locker.isTaken())
                count++;
        }
        return count + 1;
    }


}
