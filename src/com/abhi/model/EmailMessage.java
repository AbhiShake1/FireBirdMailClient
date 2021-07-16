package com.abhi.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.internet.MimeBodyPart;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailMessage {

    private final SimpleStringProperty subject; //can be called super set of string
    private final SimpleStringProperty sender;
    private final SimpleStringProperty recipient;
    private final SimpleObjectProperty<SizeInteger> size;
    private final SimpleObjectProperty<Date> date;
    private boolean isRead;
    private final Message message;
    private final List<MimeBodyPart> attachmentList = new ArrayList<>();
    private boolean hasAttachments; //to reduce execution time

    public  EmailMessage(String subject, String sender, String recipient, int size, Date date, boolean isRead, Message message){
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.size = new SimpleObjectProperty<>(new SizeInteger(size));
        this.date = new SimpleObjectProperty<>(date);
        this.isRead = isRead;
        this.message = message;
    }

    public boolean isHasAttachments() {
        return hasAttachments;
    }

    public List<MimeBodyPart> getAttachmentList() {
        return attachmentList;
    }

    public void addAttachment(MimeBodyPart mimeBodyPart){
        hasAttachments = true;
        attachmentList.add(mimeBodyPart);

    }

    //these getters must not be removed as they are accessed using value factory in MainWindowController
    //they are not 'unused'
    public String getSubject() {
        return subject.get();
    }

    public String getSender() {
        return sender.get();
    }

    public String getRecipient() {
        return recipient.get();
    }

    public SizeInteger getSize() {
        return size.get();
    }

    public Date getDate() {
        return date.get();
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Message getMessage() {
        return message;
    }
}
