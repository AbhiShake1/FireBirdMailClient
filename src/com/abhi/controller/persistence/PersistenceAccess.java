package com.abhi.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {

    //this is where serialized object is accessed from
    //random extension so that it will be hidden in the location
    private final String DIRECTORY_VALID_ACCOUNTS = System.getProperty("user.home")+"/.xyz";

    public List<ValidAccount> loadAccounts(){//invoke when app starts
        List<ValidAccount> resultList = new ArrayList<>();
        try{
            FileInputStream inputStream = new FileInputStream(DIRECTORY_VALID_ACCOUNTS);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            List<ValidAccount> persistentList = (List<ValidAccount>) objectInputStream.readObject();
            resultList.addAll(persistentList);
        }catch (Exception ignored){}
        return resultList;
    }

    public void saveAccounts(List<ValidAccount> validAccounts){//invoke when app stops
        File file = new File(DIRECTORY_VALID_ACCOUNTS);
        try{
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            //save
            objectOutputStream.writeObject(validAccounts);
            outputStream.close();
            objectOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
