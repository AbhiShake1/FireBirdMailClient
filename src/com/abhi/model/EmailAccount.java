package com.abhi.model;

import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class EmailAccount {

    private final String address;
    private final String password;
    private final Properties properties; //to hold email configs
    private Store store;
    private Session session;

    public EmailAccount(String address, String password) {
        this.address = address;
        this.password = password;
        properties = new Properties();
        //from google
        properties.put("incomingHost", "imap.gmail.com"); //host that will be connected by client
        properties.put("mail.store.protocol", "imaps"); //protocol for sending emails
        // protocol for retrieving emails
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("outgoingHost", "smtp.gmail.com");
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return address;
    }
}
