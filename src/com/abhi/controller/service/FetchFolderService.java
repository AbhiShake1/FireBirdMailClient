package com.abhi.controller.service;

import com.abhi.model.EmailTreeItem;
import com.abhi.view.IconResolver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.List;

public class FetchFolderService extends Service<Void> { //void return type. 'Void' is a special wrapper class

    private final Store store;
    private final EmailTreeItem foldersRoot;

    private final List<Folder> folderList;

    private final IconResolver iconResolver = new IconResolver();

    public FetchFolderService(Store store, EmailTreeItem treeItem, List<Folder> folderList) {
        this.store = store;
        foldersRoot = treeItem;
        this.folderList = folderList;
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

    private void handleFolders(Folder[] folders, EmailTreeItem foldersRoot) throws MessagingException {
        for(Folder folder : folders){
            folderList.add(folder);
            EmailTreeItem emailTreeItem = new EmailTreeItem(folder.getName());
            emailTreeItem.setGraphic(iconResolver.getIconForFolder(folder.getName()));
            foldersRoot.getChildren().add(emailTreeItem);
            foldersRoot.setExpanded(true);
            fetchMessageOnFolder(folder, emailTreeItem);
            addMessageListenerToFolder(folder, emailTreeItem);
            if(folder.getType()==Folder.HOLDS_FOLDERS){
                Folder[] subFolders = folder.list();
                handleFolders(subFolders, emailTreeItem); //performance untested. recursion might make it slow
            }
        }
    }

    private void addMessageListenerToFolder(Folder folder, EmailTreeItem emailTreeItem){
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent messageCountEvent) {
                for(int i=0;i<messageCountEvent.getMessages().length;i++){
                    try {
                        Message message = folder.getMessage(folder.getMessageCount()-i);
                        emailTreeItem.addEmailStack(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void messagesRemoved(MessageCountEvent messageCountEvent) {

            }
        });
    }

    private void fetchMessageOnFolder(Folder folder, EmailTreeItem emailTreeItem) {
        Service<Void> fetchFolderService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
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
