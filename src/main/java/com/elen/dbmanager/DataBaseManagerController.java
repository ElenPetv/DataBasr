package com.elen.dbmanager;

import com.elen.dbmanager.manager.DatabaseManager;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataBaseManagerController {

    @FXML
    private Button aboutProgram;

    @FXML
    private Button addColumn;

    @FXML
    private Button addData;

    @FXML
    private TableView<?> mainTable;

    @FXML
    private TextField searchField;

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private Disposable dataSubscription;

    @FXML
    void initialize() {
        dataSubscription = databaseManager.getData()
                .distinct()
                .subscribe(this::buildTable);
    }

    public void dispose() {
        if (dataSubscription != null) {
            dataSubscription.dispose();
        }
    }

    private void buildTable(List<List<String>> lists) {

    }

    @FXML
    void onCreateNewDocument() {
        File file = createFileChooser().showSaveDialog(null);
        try {
            if (file.createNewFile()) {
                databaseManager.loadFromFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onOpenDocument() {
        File file = createFileChooser().showOpenDialog(null);
        databaseManager.loadFromFile(file);
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV Document", "*.csv"));
        return fileChooser;
    }
}
