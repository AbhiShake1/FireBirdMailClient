package com.abhi.controller;

import com.abhi.EmailManager;
import com.abhi.controller.service.MessageRendererService;
import com.abhi.model.EmailMessage;
import com.abhi.model.EmailTreeItem;
import com.abhi.model.SizeInteger;
import com.abhi.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @FXML
    void composeMessageAction() {
        viewFactory.showComposeMessageWindow();
    }

    private MessageRendererService messageRendererService;

    private final MenuItem markUnReadMenuItem = new MenuItem("mark as unread");

    private final MenuItem deleteMessageMenuItem = new MenuItem("delete message");

    private final MenuItem showMessageDetailsMenuItem = new MenuItem("view details");

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

        emailsTableView.setContextMenu(new ContextMenu(markUnReadMenuItem, deleteMessageMenuItem, showMessageDetailsMenuItem));
    }

    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(e->{ //click of mouse, touchpad
            EmailTreeItem item = (EmailTreeItem) emailsTreeView.getSelectionModel().getSelectedItem();
            if(item != null){
                emailManager.setSelectedFolder(item);
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
                emailManager.setSelectedMessage(emailMessage);
                if(!emailMessage.isRead()) emailManager.setRead();
                emailManager.setSelectedMessage(emailMessage);
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart(); //start() can not be called twice in sequence anyhow
            }
        });
    }

    private void setUpContextMenu() {
        markUnReadMenuItem.setOnAction(e->emailManager.setUnRead());
        deleteMessageMenuItem.setOnAction(e->{
            emailManager.deleteMessage();
            emailsWebView.getEngine().loadContent(""); //not be able to visualize message after deleting
        });
        showMessageDetailsMenuItem.setOnAction(e->viewFactory.showEmailDetailsWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
        setUpTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRendererService();
        setUpMessageSelection();
        setUpContextMenu();
    }
}
