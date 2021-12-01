package com.database;

import com.gluonhq.charm.glisten.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

public class DataBaseManager {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button aboutProgram;

    @FXML
    private Button addColumn;

    @FXML
    private Button addData;

    @FXML
    private TableView<?> mainTable;

    @FXML
    private MenuItem menuCloseFile;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuNewFile;

    @FXML
    private MenuItem menuOpenFile;

    @FXML
    private MenuItem menuSaveFile;

    @FXML
    private MenuItem munuSaveAsFile;

    @FXML
    private TextField searchField;

    @FXML
    void initialize() {
        assert aboutProgram != null : "fx:id=\"aboutProgram\" was not injected: check your FXML file 'table.fxml'.";
        assert addColumn != null : "fx:id=\"addColumn\" was not injected: check your FXML file 'table.fxml'.";
        assert addData != null : "fx:id=\"addData\" was not injected: check your FXML file 'table.fxml'.";
        assert mainTable != null : "fx:id=\"mainTable\" was not injected: check your FXML file 'table.fxml'.";
        assert menuCloseFile != null : "fx:id=\"menuCloseFile\" was not injected: check your FXML file 'table.fxml'.";
        assert menuFile != null : "fx:id=\"menuFile\" was not injected: check your FXML file 'table.fxml'.";
        assert menuNewFile != null : "fx:id=\"menuNewFile\" was not injected: check your FXML file 'table.fxml'.";
        assert menuOpenFile != null : "fx:id=\"menuOpenFile\" was not injected: check your FXML file 'table.fxml'.";
        assert menuSaveFile != null : "fx:id=\"menuSaveFile\" was not injected: check your FXML file 'table.fxml'.";
        assert munuSaveAsFile != null : "fx:id=\"munuSaveAsFile\" was not injected: check your FXML file 'table.fxml'.";
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'table.fxml'.";

    }

}
