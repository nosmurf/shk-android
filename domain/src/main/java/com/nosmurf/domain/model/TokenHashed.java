package com.nosmurf.domain.model;

/**
 * Created by Sergio on 01/12/2016.
 */

public class TokenHashed {
    private int block;

    private byte[] hash;

    public TokenHashed(int block, byte[] hash) {
        this.block = block;
        this.hash = hash;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
}
