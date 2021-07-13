package com.abhi.controller.service;

import com.abhi.EmailManager;
import com.abhi.controller.EmailLoginResult;
import com.abhi.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.*;

public class LoginService extends Service<EmailLoginResult> {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    private EmailLoginResult login(){
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };

        try{
            Session session = Session.getInstance(emailAccount.getProperties(), authenticator);
            Store store = session.getStore("imaps"); //imap protocol
            store.connect(emailAccount.getProperties().getProperty("incomingHost"),
                    emailAccount.getAddress(), emailAccount.getPassword());
            emailAccount.setStore(store);
            emailManager.addEmailAccount(emailAccount);
        }catch (NoSuchProviderException nspe){
            nspe.printStackTrace();
            return EmailLoginResult.FAILED_BY_NETWORK;
        }catch (AuthenticationFailedException afe){
            afe.printStackTrace();
            return EmailLoginResult.FAILED_BY_CREDENTIALS;
        }catch (MessagingException me){
            me.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }catch (Exception e){
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }
        //if noting goes wrong
        return EmailLoginResult.SUCCESS;
    }

    @Override
    protected Task<EmailLoginResult> createTask() {
        return new Task<>() {
            @Override
            protected EmailLoginResult call() {
                return login();
            }
        };
    }
}
