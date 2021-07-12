package com.abhi.view;

import com.abhi.EmailManager;
import com.abhi.controller.BaseController;
import com.abhi.controller.LoginWindowController;
import com.abhi.controller.MainWindowController;
import com.abhi.controller.OptionsWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    private EmailManager emailManager;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public void showLoginWindow(){
        BaseController controller = new LoginWindowController(emailManager, this, "LoginWindow.fxml");
        initializeStage(controller);
    }

    public void showMainWindow(){
        BaseController controller = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initializeStage(controller);
    }

    public void showOptionsWindow(){
        BaseController controller = new OptionsWindowController(emailManager, this, "OptionsWindow.fxml");
        initializeStage(controller);
    }

    //handling view options
    private BackgroundTheme theme = BackgroundTheme.DEFAULT;
    private FontSize fontSize = FontSize.MEDIUM;

    public BackgroundTheme getBackgroundTheme() {
        return theme;
    }

    public void setBackgroundTheme(BackgroundTheme theme) {
        this.theme = theme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    private void initializeStage(BaseController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);
        Parent parent;
        try{
            parent = fxmlLoader.load();
        }catch (IOException ioe){
            ioe.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void close(Stage stage){
        stage.close(); //close previous stage to avoid having 2 stages opened at once
    }
}
