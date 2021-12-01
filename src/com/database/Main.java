package com.database;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Objects;

public class Main extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("table.fxml")));
        stage.setTitle("Учет запчастей");
        stage.setScene(new Scene(root, 900,600));
        stage.show();

    }
    public static void main(String[] args) {

        launch(args);
    }
}