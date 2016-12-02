package com.nosmurf.domain.model;

/**
 * Created by Sergio on 02/12/2016.
 */

public class Key {
    private int sector;

    private int block;

    private String value;

    public Key(int sector, int block, String value) {
        this.sector = sector;
        this.block = block;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }
}
