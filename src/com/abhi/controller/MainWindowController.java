package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.controller.service.MessageRendererService;
import com.abhi.model.EmailMessage;
import com.abhi.model.EmailTreeItem;
import com.abhi.model.SizeInteger;
import com.abhi.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private TreeView<String> emailsTreeView;

    @FXML
    private TableView<EmailMessage> emailsTableView;

    @FXML
    private WebView emailsWebView;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    private TableColumn<EmailMessage, String> recipientCol;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

    @FXML
    void optionsAction() {
        viewFactory.showOptionsWindow();
    }

    @FXML
    void addAccountAction() {
        viewFactory.showLoginWindow();
    }

    private MessageRendererService messageRendererService;

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFolderRoot());
        emailsTreeView.setShowRoot(false);
    }

    private void setUpTableView() {
        senderCol.setCellValueFactory(new PropertyValueFactory<>("Sender"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("Subject"));
        recipientCol.setCellValueFactory(new PropertyValueFactory<>("Recipient"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("Size"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }

    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(e->{ //click of mouse, touchpad
            EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();
            if(item != null){
                emailsTableView.setItems(item.getEmailMessages());
            }
        });
    }

    private void setUpBoldRows() {
        emailsTableView.setRowFactory(t-> new TableRow<>() {
            @Override
            protected void updateItem(EmailMessage item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null){
                    if(item.isRead()){
                        setStyle("");
                    }else {
                        setStyle("-fx-font-weight: bold");
                    }
                }
            }
        });
    }

    private void setUpMessageRendererService() {
        messageRendererService = new MessageRendererService(emailsWebView.getEngine());
    }

    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(e->{
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
            if(emailMessage != null){
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart(); //start() can not be called twice in sequence anyhow
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
        setUpTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRendererService();
        setUpMessageSelection();
    }
}
