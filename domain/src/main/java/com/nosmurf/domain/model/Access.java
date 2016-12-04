package com.nosmurf.domain.model;

import java.util.Date;

/**
 * Created by Daniel on 03/12/2016.
 */

public class Access {

    String displayName;

    Date date;

    boolean nfc;

    boolean face;

    public Access() {

    }

    public Access(String displayName, Date date, boolean nfc, boolean face) {
        this.displayName = displayName;
        this.date = date;
        this.nfc = nfc;
        this.face = face;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
