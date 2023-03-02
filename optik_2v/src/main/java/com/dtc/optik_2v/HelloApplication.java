package com.dtc.optik_2v;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.setScene(scene);
        Image img = new Image(this.getClass().getResource("image/logo.png").toURI().toString());
        stage.getIcons().add(img);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}