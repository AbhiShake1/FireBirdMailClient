package com.abhi.controller.service;

import com.abhi.controller.EmailSendingResult;
import com.abhi.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;

public class EmailSenderService extends Service<EmailSendingResult> {

    private final EmailAccount emailAccount;
    private final String subject, recipient, content;

    private final List<File> attachments;

    public EmailSenderService(EmailAccount emailAccount, String subject, String recipient, String content, List<File> attachments) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments = attachments;
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
                    //adding attachments
                    attachments.forEach(file->{
                        MimeBodyPart mimeBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(file.getAbsolutePath());
                        try {//need another try catch since lambda creates another class in bytecode
                            //intentionally done to not break when exception occurs
                            mimeBodyPart.setDataHandler(new DataHandler(source));
                            mimeBodyPart.setFileName(file.getName());
                            multipart.addBodyPart(mimeBodyPart);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    });
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
