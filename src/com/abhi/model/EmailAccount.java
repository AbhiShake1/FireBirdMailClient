package com.abhi.model;

import javax.mail.Store;
import java.util.Properties;

public class EmailAccount {

    private final String address;
    private final String password;
    private Properties properties; //to hold email configs
    private Store store;

    public EmailAccount(String address, String password) {
        this.address = address;
        this.password = password;
        properties = new Properties();
        //from google
        properties.put("incomingHost","imap.gmail.com"); //host that will be connected by client
        properties.put("mail.store.protocol","imaps"); //protocol for sending emails
        // protocol for retrieving emails
        properties.put("mail.transparent.protocol","smtps");
        properties.put("mail.smtps.host","smtp.gmail.com");
        properties.put("ongoingHost","smtp.gmail.com");
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
