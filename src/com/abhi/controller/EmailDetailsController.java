package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.controller.service.MessageRendererService;
import com.abhi.model.EmailMessage;
import com.abhi.view.ViewFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

public class EmailDetailsController extends BaseController implements Initializable {

    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private Label attachmentsLabel;

    @FXML
    private HBox hBoxDownloads;

    private final String DIRECTORY_DOWNLOADS = System.getProperty("user.home")+"/Downloads/";

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    private void loadAttachments(EmailMessage emailMessage) {
        if(emailMessage.isHasAttachments()){
            //add buttons based on number of attachments
            List<MimeBodyPart> attachmentList = emailMessage.getAttachmentList();
            //remove duplicate elements
            HashSet<MimeBodyPart> attachments = new HashSet<>(attachmentList);
            attachments.forEach(l->{
                try {
                    Button button = new AttachmentButton(l);
                    hBoxDownloads.getChildren().add(button);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
        }else attachmentsLabel.setText(""); //hide
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailMessage emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());


        loadAttachments(emailMessage);

        MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
        messageRendererService.setEmailMessage(emailMessage); //tell which message to render
        messageRendererService.restart();
    }

    private class AttachmentButton extends Button{

        private final String downloadedFilePath;

        private final MimeBodyPart mimeBodyPart;

        public AttachmentButton(MimeBodyPart mimeBodyPart) throws MessagingException {
            this.mimeBodyPart = mimeBodyPart;
            StringBuilder downloadText = new StringBuilder(mimeBodyPart.getFileName());
            downloadedFilePath = DIRECTORY_DOWNLOADS + downloadText;
            if(new File(downloadedFilePath).exists()) downloadText.append("(open)");
            setText(downloadText.toString());
            setOnAction(e->downloadAttachment());
        }

        private void downloadAttachment(){
            setDisabled(true); //dont allow clicking buttons while content is downloading
            //multithreading since the process might take a long time
            Service<Void> service = new Service<>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            File file = new File(downloadedFilePath);
                            Desktop desktop = Desktop.getDesktop();
                            if(file.exists()){
                                try {
                                    desktop.open(file); //might have problems with mac
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            }else{
                                setText("Downloading..");
                                mimeBodyPart.saveFile(downloadedFilePath);
                            }
                            return null;
                        }
                    };
                }
            };
            service.setOnSucceeded(e->{
                try {
                    setText(mimeBodyPart.getFileName() + "(open)");
                    setDisabled(false);
                } catch (MessagingException messagingException) {
                    messagingException.printStackTrace();
                }
            });
            //start() can not be used since a thread can not be started twice and the function can be invoked many times
            service.restart();
        }
    }
}
