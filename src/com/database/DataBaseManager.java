package com.database;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

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
    private MenuItem menuCreateFile;

    @FXML
    private MenuItem menuDeleteFile;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuOpenFile;

    @FXML
    private TextField searchField;

    @FXML
    void initialize() {
        assert aboutProgram != null : "fx:id=\"aboutProgram\" was not injected: check your FXML file 'table.fxml'.";
        assert addColumn != null : "fx:id=\"addColumn\" was not injected: check your FXML file 'table.fxml'.";
        assert addData != null : "fx:id=\"addData\" was not injected: check your FXML file 'table.fxml'.";
        assert mainTable != null : "fx:id=\"mainTable\" was not injected: check your FXML file 'table.fxml'.";
        assert menuCreateFile != null : "fx:id=\"menuCreateFile\" was not injected: check your FXML file 'table.fxml'.";
        assert menuDeleteFile != null : "fx:id=\"menuDeleteFile\" was not injected: check your FXML file 'table.fxml'.";
        assert menuFile != null : "fx:id=\"menuFile\" was not injected: check your FXML file 'table.fxml'.";
        assert menuOpenFile != null : "fx:id=\"menuOpenFile\" was not injected: check your FXML file 'table.fxml'.";
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'table.fxml'.";

    }

}
