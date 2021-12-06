package com.elen.dbmanager;

import com.elen.dbmanager.manager.DatabaseManager;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DbManagerApplication extends Application {
    private DataBaseManagerController appController;
    private Disposable titleChangesSubscription;

    @Override
    public void start(Stage mainStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DbManagerApplication.class.getResource("main.fxml"));
        appController = fxmlLoader.getController();
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        mainStage.setScene(scene);
        mainStage.show();


        titleChangesSubscription = DatabaseManager.getInstance()
                .dbFile()
                .subscribe(optionalFile -> {
                    if (optionalFile.isPresent()) {
                        mainStage.setTitle("DataBase Manager - " + optionalFile.get());
                    } else {
                        mainStage.setTitle("DataBase Manager");
                    }
                });
    }

    @Override
    public void stop() {
        if (appController != null) {
            appController.dispose();
        }
        if (titleChangesSubscription != null) {
            titleChangesSubscription.dispose();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}