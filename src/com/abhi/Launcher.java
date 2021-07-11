package com.abhi;

import com.abhi.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        ViewFactory viewFactory = new ViewFactory(new EmailManager());
        viewFactory.showLoginWindow();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
