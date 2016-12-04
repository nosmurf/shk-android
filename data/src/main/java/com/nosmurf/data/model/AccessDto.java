package com.nosmurf.data.model;

/**
 * Created by Daniel on 03/12/2016.
 */

public class AccessDto {

    String uid;

    long datetime;

    boolean nfc;

    boolean face;

    public AccessDto() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public boolean isNfc() {
        return nfc;
    }

    public void setNfc(boolean nfc) {
        this.nfc = nfc;
    }

    public boolean isFace() {
        return face;
    }

    public void setFace(boolean face) {
        this.face = face;
    }
}
