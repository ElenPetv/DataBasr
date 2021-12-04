package com.elen.dbmanager;

import com.elen.dbmanager.manager.DatabaseManager;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseManagerController {

    @FXML
    private Button aboutProgram;

    @FXML
    private Button addColumn;

    @FXML
    private Button addData;

    @FXML
    private TableView<Map> mainTable;

    @FXML
    private TextField searchField;

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private Disposable dataSubscription;
    public final ObservableList<Map> tableItems = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        mainTable.setItems(tableItems);
        dataSubscription = databaseManager.getData()
                .distinctUntilChanged()
                .subscribe(this::buildTable);
    }

    public void dispose() {
        if (dataSubscription != null) {
            dataSubscription.dispose();
        }
    }

    private void buildTable(List<List<String>> lists) {
        ObservableList<TableColumn<Map, ?>> columns = mainTable.getColumns();
        columns.clear();
        tableItems.clear();
        if (lists.isEmpty()) {
            return;
        }
        var controlsColumn = new TableColumn<Map, String>("");
        controlsColumn.setCellValueFactory(new MapValueFactory<>("controls"));
        controlsColumn.setSortable(false);
        columns.add(controlsColumn);

        for (int i = 0; i < lists.size(); i++) {
            List<String> row = lists.get(i);
            if (i == 0) {
                for (int j = 0; j < row.size(); j++) {
                    String item = row.get(j);
                    var column = new TableColumn<Map, String>(item);
                    column.setCellValueFactory(new MapValueFactory<>(j));
                    columns.add(column);
                }
            } else {
                Map<Object, String> map = new HashMap<>();
                map.put("controls", "");
                for (int j = 0; j < row.size(); j++) {
                    map.put(j, row.get(j));
                }
                tableItems.add(map);
            }
        }
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
