package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.controller.service.EmailSenderService;
import com.abhi.model.EmailAccount;
import com.abhi.view.ViewFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {

    @FXML
    private TextField recipientTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Label errorLabel;

    @FXML
    private Button sendButton;

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    private final List<File> attachments = new ArrayList<>();

    @FXML
    private void sendButtonAction() {
        EmailSenderService emailSenderService = new EmailSenderService(
                emailAccountChoice.getValue(),
                subjectTextField.getText(),
                recipientTextField.getText(),
                htmlEditor.getHtmlText(),
                attachments
        );
        emailSenderService.start();
        emailSenderService.setOnSucceeded(e->{
            EmailSendingResult emailSendingResult = emailSenderService.getValue();
            switch (emailSendingResult) {
                case SUCCESS -> {
                    Stage stage = (Stage) sendButton.getScene().getWindow();
                    viewFactory.close(stage);
                }
                case FAILED_BY_PROVIDER -> errorLabel.setText("Provider error");
                case FAILED_BY_UNEXPECTED_ERROR -> errorLabel.setText("Unexpected error");
            }
        });
    }

    @FXML
    private void attachButtonAction(){
        //open file chooser
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null) attachments.add(selectedFile);
    }

    public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<EmailAccount> account = emailManager.getEmailAccounts();
        emailAccountChoice.setItems(account);
        emailAccountChoice.setValue(account.get(0));
    }
}
