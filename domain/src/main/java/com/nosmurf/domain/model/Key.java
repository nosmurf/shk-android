package com.nosmurf.domain.model;

/**
 * Created by Sergio on 02/12/2016.
 */

public class Key {
    private int sector;

    private byte[] value;

    public Key(int sector, byte[] value) {
        this.sector = sector;
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }
}
