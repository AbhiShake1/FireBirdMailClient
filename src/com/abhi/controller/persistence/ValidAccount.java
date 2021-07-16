package com.abhi.controller.persistence;

import java.io.Serializable;

public class ValidAccount implements Serializable { //let class object be saved to bytestream

    private final String address;
    private String password; //only encrypt password while saving

    public ValidAccount(String address, String password) {
        this.address = address;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
