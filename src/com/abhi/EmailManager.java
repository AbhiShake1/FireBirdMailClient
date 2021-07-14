package com.abhi;

import com.abhi.controller.service.FetchFolderService;
import com.abhi.controller.service.FolderUpdaterService;
import com.abhi.model.EmailAccount;
import com.abhi.model.EmailTreeItem;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    FolderUpdaterService folderUpdaterService;

    //handling folders
    private final EmailTreeItem<String> folderRoot = new EmailTreeItem<>("");

    private final List<Folder> folderList = new ArrayList<>();

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public EmailTreeItem<String> getFolderRoot() {
        return folderRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount){
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();
        folderRoot.getChildren().add(treeItem);
    }
}
