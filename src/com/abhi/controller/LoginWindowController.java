package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.controller.service.LoginService;
import com.abhi.model.EmailAccount;
import com.abhi.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginWindowController extends BaseController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    CheckBox showPasswordCheckBox;

    @FXML
    TextField passwordFieldVisible;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private void showPasswordAction() {
        if(showPasswordCheckBox.isSelected()){
            passwordFieldVisible.setText(passwordField.getText());
            passwordFieldVisible.setVisible(true);
        }else {
            passwordField.setText(passwordFieldVisible.getText());
            passwordFieldVisible.setVisible(false);
        }
    }

    @FXML
    private void loginButtonAction() {
        if(isFieldValid()){
            EmailAccount emailAccount = new EmailAccount(emailField.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount,emailManager);
            loginService.start(); //start background task thread to avoid unresponsive UI or a small time while logging in
            loginButton.setText("Logging in..."); //change text after button is clicked
            loginService.setOnSucceeded(e->{
                EmailLoginResult emailLoginResult = loginService.getValue();
                switch (emailLoginResult) {
                    case SUCCESS -> {
                        if (!viewFactory.isMainViewInitialized()) {
                            viewFactory.showMainWindow();
                        }
                        //using any one of the login scene elements to get stage out of it
                        Stage stage = (Stage) emailField.getScene().getWindow();
                        //close previous stage to avoid having 2 stages opened at once
                        viewFactory.close(stage); //link all view actions to ViewFactory instead of writing here to maintain proper pattern
                    }
                    case FAILED_BY_CREDENTIALS -> errorLabel.setText("Invalid credentials");
                    case FAILED_BY_UNEXPECTED_ERROR -> errorLabel.setText("Unexpected error");
                }
            });
        }
    }

    private boolean isFieldValid(){
        if(emailField.getText().isBlank()){//remove wide spaces before checking empty
            errorLabel.setText("Please enter email.");
            return false;
        }
        if(passwordField.getText().isBlank()){//remove wide spaces before checking empty
            errorLabel.setText("Please enter email.");
            return false;
        }
        return true;
    }

}
