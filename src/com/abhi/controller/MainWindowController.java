package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private TreeView<String> emailsTreeView;

    @FXML
    private TableView<?> emailsTableView;

    @FXML
    private WebView emailsWebView;

    @FXML
    void optionsAction() {
        viewFactory.showOptionsWindow();
    }

    @FXML
    void addAccountAction() {
        viewFactory.showLoginWindow();
    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFolderRoot());
        emailsTreeView.setShowRoot(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
    }
}
