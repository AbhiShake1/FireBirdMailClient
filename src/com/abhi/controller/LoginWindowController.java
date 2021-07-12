package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController extends BaseController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction(ActionEvent event) {
        viewFactory.showMainWindow();
        //using any one of the login scene elements to get stage out of it
        Stage stage = (Stage) emailField.getScene().getWindow();
        //close previous stage to avoid having 2 stages opened at once
        viewFactory.close(stage); //link all view actions to ViewFactory instead of writing here to maintain proper pattern
    }

}
