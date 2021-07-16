package com.abhi.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.function.Function;

public class IconResolver {

    public Node getIconForFolder(String folderName){
        folderName = folderName.toLowerCase();
        ImageView imageView;
        Function<String, ImageView> icon = i->new ImageView(new Image(getClass().getResourceAsStream("icon/"+i+".png")));
        try{
            if(folderName.contains("@")){//is email address
                imageView = icon.apply("email");

            }else if(folderName.contains("inbox")){
                imageView = icon.apply("inbox");

            }else if(folderName.contains("sent")){
                imageView = icon.apply("sent2");

            }else if(folderName.contains("spam")){
                imageView = icon.apply("spam");

            }else{
                imageView = icon.apply("folder");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        //scale images of different sizes
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        return imageView;
    }
}
