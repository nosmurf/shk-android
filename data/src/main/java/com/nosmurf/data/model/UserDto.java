package com.nosmurf.data.model;

/**
 * Created by Daniel on 02/12/2016.
 */

public class UserDto {

    String name;

    public UserDto() {

    }

    public UserDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
