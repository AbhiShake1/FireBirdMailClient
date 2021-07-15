package com.abhi;

import com.abhi.controller.service.FetchFolderService;
import com.abhi.controller.service.FolderUpdaterService;
import com.abhi.model.EmailAccount;
import com.abhi.model.EmailMessage;
import com.abhi.model.EmailTreeItem;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private EmailMessage selectedMessage;

    private EmailTreeItem<String> selectedFolder;

    FolderUpdaterService folderUpdaterService;

    //handling folders
    private final EmailTreeItem<String> folderRoot = new EmailTreeItem<>("");

    private final List<Folder> folderList = new ArrayList<>();

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
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

    public void setRead() {
        try{
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessagesCount();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUnRead() {
        try{
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);//server side
            selectedFolder.incrementMessagesCount();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteMessage() {
        try{
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true); //say that msg was deleted to server
            selectedFolder.getEmailMessages().remove(selectedMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
