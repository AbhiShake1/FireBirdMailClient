package com.abhi.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailTreeItem extends TreeItem<String> {

    private final String name;
    private final ObservableList<EmailMessage> emailMessages;
    private int unreadMessagesCount;

    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();
    }

    public ObservableList<EmailMessage> getEmailMessages() {
        return emailMessages;
    }

    public void addEmail(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        emailMessages.add(emailMessage);
    }

    public void addEmailStack(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        emailMessages.add(0,emailMessage);
    }

    private EmailMessage fetchMessage(Message message) throws MessagingException {
        boolean messageRead = message.getFlags().contains(Flags.Flag.SEEN);
        EmailMessage emailMessage = new EmailMessage(
                message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(Message.RecipientType.TO)[0].toString(),
                message.getSize(),
                message.getSentDate(),
                messageRead,
                message
        );
        if(!messageRead) incrementMessagesCount();
        return emailMessage;
    }

    public void incrementMessagesCount(){
        unreadMessagesCount++;
        updateName();
    }

    public void decrementMessagesCount(){
        unreadMessagesCount--;
        updateName();
    }

    private void updateName(){
        if(unreadMessagesCount>0){
            setValue(name+"("+unreadMessagesCount+")");
        }else{
            setValue(name);
        }
    }
}
