package com.abhi.controller.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import java.util.List;

public class FolderUpdaterService extends Service {

    private List<Folder> folderList;

    public FolderUpdaterService(List<Folder> folderList) {
        this.folderList = folderList;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() {
                while(true){//infinite since it always needs to run and scan at runtime
                    //to not break the loop even when exception is thrown
                    try {
                        Thread.sleep(3000); //refresh interval. sleep for 3 seconds
                        for (Folder folder : folderList) {
                            if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
                                folder.getMessageCount();
                            }
                        }
                    }catch (Exception ignored){}
                }
            }
        };
    }
}
