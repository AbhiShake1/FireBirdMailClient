package com.abhi.controller.service;

import com.abhi.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class MessageRendererService extends Service<String> {

    private EmailMessage emailMessage;
    //visualize with help of webview
    private final WebEngine webEngine;
    private final StringBuffer stringBuffer; //for holding content rendered by webview

    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        stringBuffer = new StringBuffer();
        setOnSucceeded(e->displayMessage());
    }

    public void setEmailMessage(EmailMessage emailMessage){
        this.emailMessage = emailMessage;
    }

    private void loadMessage() throws MessagingException, IOException {
        //since method is intended to be used many times
        stringBuffer.setLength(0); //clear the StringBuffer
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();
        if(isSimpleType(contentType)){
            stringBuffer.append(message.getContent().toString());
        } else if(isMultipartType(contentType)){
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for (int i = multipart.getCount() - 1; i >= 0; i--) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
            if (isSimpleType(contentType)) {
                stringBuffer.append(bodyPart.getContent().toString());
            } else if (isMultipartType(contentType)) {
                Multipart multipart2 = (Multipart) bodyPart.getContent();
                loadMultipart(multipart2, stringBuffer);
            } else if (!isTextPlain(contentType)) {//get attachments
                MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(mimeBodyPart);
            }
        }
    }

    private boolean isSimpleType(String contentType){
        return contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text");
    }

    private boolean isMultipartType(String contentType){
        return contentType.contains("multipart");
    }

    private boolean isTextPlain(String contentType){
        return contentType.contains("TEXT/PLAIN");
    }

    private void displayMessage(){
        webEngine.loadContent(stringBuffer.toString());
    }

    @Override
    protected Task<String> createTask() {
        return new Task<>() {
            @Override
            protected String call() {
                try {
                    loadMessage();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
