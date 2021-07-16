package com.abhi;

import com.abhi.controller.persistence.PersistenceAccess;
import com.abhi.controller.persistence.ValidAccount;
import com.abhi.controller.service.LoginService;
import com.abhi.model.EmailAccount;
import com.abhi.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    private final PersistenceAccess persistenceAccess = new PersistenceAccess();
    private final EmailManager emailManager = new EmailManager();

    @Override
    public void start(Stage primaryStage) {
        ViewFactory viewFactory = new ViewFactory(emailManager);
        checkPersistence(viewFactory);
    }

    private void checkPersistence(ViewFactory viewFactory) {
        List<ValidAccount> validAccounts = persistenceAccess.loadAccounts();
        if(validAccounts.size()>0){ //if accounts exist
            viewFactory.showMainWindow();
            validAccounts.forEach(e->{
                EmailAccount emailAccount = new EmailAccount(e.getAddress(), e.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            });
        }else{
            viewFactory.showLoginWindow();
        }
    }

    @Override
    public void stop() {
        List<ValidAccount> validAccounts = new ArrayList<>();
        emailManager.getEmailAccounts().forEach(e->validAccounts.add(new ValidAccount(e.getAddress()
                ,e.getPassword())
        ));
        persistenceAccess.saveAccounts(validAccounts);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
