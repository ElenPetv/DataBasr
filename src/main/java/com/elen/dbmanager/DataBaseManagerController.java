package com.elen.dbmanager;

import com.elen.dbmanager.manager.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

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
    private MenuItem menuCreateFile;

    @FXML
    private MenuItem menuDeleteFile;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuOpenFile;

    @FXML
    private TextField searchField;

    private DatabaseManager databaseManager;

    @FXML
    void onCreateNewDocument() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV Document", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        try {
            if (file.createNewFile()) {
                databaseManager = new DatabaseManager(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
