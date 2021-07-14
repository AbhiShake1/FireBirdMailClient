package com.abhi.controller.service;

import com.abhi.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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
            stringBuffer.append(message.getContent());
        }else if(isMultipartType(contentType)){
            Multipart multipart = (Multipart) message.getContent();
            for(int i = multipart.getCount()-1;i >= 0;i--){
                BodyPart bodyPart = multipart.getBodyPart(i);
                String bodyPartContentType = bodyPart.getContentType();
                if(isSimpleType(bodyPartContentType)){
                    stringBuffer.append(bodyPart.getContent());
                }
            }
        }
    }

    private boolean isSimpleType(String contentType){
        return contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text");
    }

    private boolean isMultipartType(String contentType){
        return contentType.contains("multipart");
    }

    private void displayMessage(){
        webEngine.loadContent(stringBuffer.toString());
    }

    @Override
    protected Task createTask() {
        return new Task<>() {
            @Override
            protected Object call() {
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
