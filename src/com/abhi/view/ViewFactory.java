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
import java.util.ArrayList;
import java.util.List;

public class ViewFactory {

    private EmailManager emailManager;

    private List<Stage> activeStages;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<>();
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
        activeStages.add(stage);
    }

    public void close(Stage stage){
        stage.close(); //close previous stage to avoid having 2 stages opened at once
        activeStages.remove(stage);
    }

    public void updateStyles() {
        for (Stage stage : activeStages){
            Scene scene = stage.getScene();
            scene.getStylesheets().clear(); //clear previous css
            scene.getStylesheets().add(getClass().getResource(BackgroundTheme.getCssPath(theme)).toExternalForm());
            scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
        }
    }
}
