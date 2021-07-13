package com.abhi.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailTreeItem<Str> extends TreeItem<Str> {

    private final Str name;
    private final ObservableList<EmailMessage> emailMessages;
    private int unreadMessagesCount;

    public EmailTreeItem(Str name) {
        super(name);
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();
    }

    public ObservableList<EmailMessage> getEmailMessages() {
        return emailMessages;
    }

    public void addEmail(Message message) throws MessagingException {
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
        emailMessages.add(emailMessage);
        if(!messageRead) incrementMessagesCount();
    }

    public void incrementMessagesCount(){
        unreadMessagesCount++;
        updateName();
    }

    private void updateName(){
        if(unreadMessagesCount>0){
            setValue((Str) (name+"("+unreadMessagesCount+")"));
        }else{
            setValue(name);
        }
    }
}
