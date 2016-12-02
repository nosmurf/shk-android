package com.nosmurf.data.model;

/**
 * Created by Daniel on 02/12/2016.
 */

public class PersonReference {

    String currentUser;

    String groupId;

    public PersonReference(String currentUser, String groupId) {
        this.currentUser = currentUser;
        this.groupId = groupId;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
