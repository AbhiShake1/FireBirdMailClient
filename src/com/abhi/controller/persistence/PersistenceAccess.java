package com.abhi.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {
    
    private final Encoder encoder = new Encoder();

    //this is where serialized object is accessed from
    //random extension so that it will be hidden in the location
    private final String DIRECTORY_VALID_ACCOUNTS = System.getProperty("user.home")+"/.xyz";

    public List<ValidAccount> loadAccounts(){//invoke when app starts
        List<ValidAccount> resultList = new ArrayList<>();
        try{
            FileInputStream inputStream = new FileInputStream(DIRECTORY_VALID_ACCOUNTS);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            List<ValidAccount> persistentList = (List<ValidAccount>) objectInputStream.readObject();
            decodePasswords(persistentList);
            resultList.addAll(persistentList);
        }catch (Exception ignored){}
        return resultList;
    }

    private void decodePasswords(List<ValidAccount> persistentList) {
        persistentList.forEach(v->v.setPassword(encoder.decode(v.getPassword())));
    }

    private void encodePasswords(List<ValidAccount> persistentList) {
        persistentList.forEach(v->v.setPassword(encoder.encode(v.getPassword())));
    }

    public void saveAccounts(List<ValidAccount> validAccounts){//invoke when app stops
        File file = new File(DIRECTORY_VALID_ACCOUNTS);
        try{
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            encodePasswords(validAccounts);
            //save
            objectOutputStream.writeObject(validAccounts);
            outputStream.close();
            objectOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
