package com.abhi.controller.service;

import com.abhi.model.EmailTreeItem;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

public class FetchFolderService extends Service<Void> { //void return type. 'Void' is a special wrapper class

    private final Store store;
    private final EmailTreeItem<String> foldersRoot;

    public FetchFolderService(Store store, EmailTreeItem<String> treeItem) {
        this.store = store;
        foldersRoot = treeItem;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, foldersRoot);
    }

    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        for(Folder folder : folders){
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<>(folder.getName());
            foldersRoot.getChildren().add(emailTreeItem);
            foldersRoot.setExpanded(true);
            fetchMessageOnFolder(folder, emailTreeItem);
            if(folder.getType()==Folder.HOLDS_FOLDERS){
                Folder[] subFolders = folder.list();
                handleFolders(subFolders, emailTreeItem); //performance untested. recursion might make it slow
            }
        }
    }

    private void fetchMessageOnFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        Service<String> fetchFolderService = new Service<>() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        if(folder.getType() != Folder.HOLDS_FOLDERS){
                            folder.open(Folder.READ_WRITE);
                            int folderSize = folder.getMessageCount();
                            for(int i = folderSize; i > 0;i--){
                                emailTreeItem.addEmail(folder.getMessage(i));
                            }
                        }
                        return null;
                    }
                };
            }
        };
        fetchFolderService.start();
    }
}
