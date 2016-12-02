package com.nosmurf.data.model;

/**
 * Created by Daniel on 02/12/2016.
 */

public class ImageReference {

    String groupId;

    String personId;

    String imageUrl;

    public ImageReference(String groupId, String personId, String imageUrl) {
        this.groupId = groupId;
        this.personId = personId;
        this.imageUrl = imageUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

