package com.abhi;

import com.abhi.controller.service.FetchFolderService;
import com.abhi.model.EmailAccount;
import com.abhi.model.EmailTreeItem;

public class EmailManager {

    //handling folders
    private EmailTreeItem<String> folderRoot = new EmailTreeItem<>("");

    public void setFolderRoot(EmailTreeItem<String> folderRoot) {
        this.folderRoot = folderRoot;
    }

    public EmailTreeItem<String> getFolderRoot() {
        return folderRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount){
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem);
        fetchFolderService.start();
        folderRoot.getChildren().add(treeItem);
    }
}
