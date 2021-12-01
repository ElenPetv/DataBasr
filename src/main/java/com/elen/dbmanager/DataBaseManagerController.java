package com.elen.dbmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

}
