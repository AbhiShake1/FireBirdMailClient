package com.abhi.controller.service;

import com.abhi.controller.EmailSendingResult;
import com.abhi.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSenderService extends Service<EmailSendingResult> {

    private final EmailAccount emailAccount;
    private final String subject, recipient, content;

    public EmailSenderService(EmailAccount emailAccount, String subject, String recipient, String content) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
    }

    @Override
    protected Task<EmailSendingResult> createTask() {
        return new Task<>() {
            @Override
            protected EmailSendingResult call() {
                try{//making message
                    MimeMessage message = new MimeMessage(emailAccount.getSession());
                    message.setFrom(emailAccount.getAddress());
                    message.addRecipients(Message.RecipientType.TO, recipient);
                    message.setSubject(subject);
                    //setting content
                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    message.setContent(multipart);
                    //sending message
                    Transport transport = emailAccount.getSession().getTransport();
                    transport.connect(
                            emailAccount.getProperties().getProperty("outgoingHost"),
                            emailAccount.getAddress(),
                            emailAccount.getPassword()
                    );
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    return EmailSendingResult.SUCCESS;
                } catch (MessagingException me) {
                    me.printStackTrace();
                    return EmailSendingResult.FAILED_BY_PROVIDER;
                }catch (Exception e){
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_UNEXPECTED_ERROR;
                }
            }
        };
    }
}
