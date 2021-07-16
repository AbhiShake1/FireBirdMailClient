package com.abhi.controller.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import java.util.List;

public class FolderUpdaterService extends Service<Folder> {

    private final List<Folder> folderList;

    public FolderUpdaterService(List<Folder> folderList) {
        this.folderList = folderList;
    }

    @Override
    protected Task<Folder> createTask() {
        return new Task<>() {
            @Override
            protected Folder call() {
                while(true){//infinite since it always needs to run and scan at runtime
                    //to not break the loop even when exception is thrown
                    for (Folder folder : folderList) {
                        try{ //keep refreshing
                            if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
                                folder.getMessageCount();
                            }
                        }catch (Exception ignored){}//never break out through the loop
                    }
                }
            }
        };
    }
}
