package com.elen.dbmanager;

import com.elen.dbmanager.manager.DatabaseManager;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataBaseManagerController {

    @FXML
    public Button backToMain;
    @FXML
    private VBox emptyStatePane;

    @FXML
    private Button aboutProgram;

    @FXML
    private TableView<Map> mainTable;

    @FXML
    private TextField searchField;

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private Disposable dataSubscription;
    private Disposable emptyStateSubscription;
    private final ObservableList<Map> tableItems = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        mainTable.setItems(tableItems);
        dataSubscription = databaseManager.getDataStream()
                .distinctUntilChanged()
                .subscribe(this::buildTable);
        emptyStateSubscription = databaseManager.dbFile()
                .map(Optional::isEmpty)
                .subscribe(emptyStatePane::setVisible);
    }

    public void dispose() {
        if (dataSubscription != null) {
            dataSubscription.dispose();
        }
        if (emptyStateSubscription != null) {
            emptyStateSubscription.dispose();
        }
    }

    private void buildTable(List<List<String>> lists) {
        ObservableList<TableColumn<Map, ?>> columns = mainTable.getColumns();
        columns.clear();
        tableItems.clear();
        if (lists.isEmpty()) {
            return;
        }
        var controlsColumn = new TableColumn<Map, Integer>("");
        controlsColumn.setCellValueFactory(new MapValueFactory<>("controls"));
        controlsColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Button buttonEdit = new Button("Редакт.");
                    buttonEdit.setStyle("-fx-background-color: #21c9f3");
                    buttonEdit.setOnAction(event -> showEditForm(lists, item));
                    Button buttonDelete = new Button("Удалить");
                    buttonDelete.setStyle("-fx-background-color: #ffa1a1");
                    buttonDelete.setOnAction(event -> deleteRow(item, lists));
                    setGraphic(new HBox(8, buttonEdit, buttonDelete));
                }
            }
        });
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
                Map<Object, Object> map = new HashMap<>();
                map.put("controls", i);
                for (int j = 0; j < row.size(); j++) {
                    map.put(j, row.get(j));
                }
                tableItems.add(map);
            }
        }
    }

    @FXML
    private void onCreateNewDocument() {
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
    private void onOpenDocument() {
        File file = createFileChooser().showOpenDialog(null);
        databaseManager.loadFromFile(file);
    }

    @FXML
    private void onDeleteDocument() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Удалить документ?");
        alert.setContentText("Документ " + databaseManager.getDocumentPath() + " будет удален");

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                databaseManager.deleteDocument();
            }
        });
    }

    @FXML
    private void onAddColumn() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Название столбца");
        dialog.setHeaderText("Введите название столбца");
        dialog.showAndWait()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .ifPresent(name -> {
                    List<List<String>> changedLists = new ArrayList<>(databaseManager.getData());
                    for (int i = 0; i < changedLists.size(); i++) {
                        List<String> row = new ArrayList<>(changedLists.get(i));
                        row.add(i == 0 ? name : " ");
                        changedLists.set(i, row);
                    }
                    saveChanges(changedLists);
                });
    }

    @FXML
    public void onAboutProgram(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("aboutProgram.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();

        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("О программе");
        stage.setScene(new Scene(root1));
        stage.showAndWait();

    };



    @FXML
    private void onAddRow() {
        showEditForm(databaseManager.getData(), -1);
    }

    private void deleteRow(int item, List<List<String>> lists) {
        List<List<String>> changedLists = new ArrayList<>(lists);
        changedLists.remove(item);
        saveChanges(changedLists);
    }

    /**
     * Show form for edit or create new row in table
     *
     * @param lists current table data
     * @param index index for changing row or -1 for creating new row
     */
    private void showEditForm(List<List<String>> lists, int index) {
        List<List<String>> changedLists = new ArrayList<>(lists);
        EditRowForm.show(
                index != -1 ? changedLists.get(index) : null,
                changedLists.get(0),
                row -> {
                    if (index != -1) {
                        changedLists.set(index, row);
                    } else {
                        changedLists.add(row);
                    }
                    saveChanges(changedLists);
                });
    }

    private void saveChanges(List<List<String>> lists) {
        try {
            databaseManager.saveData(lists);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось сохранить документ " + databaseManager.getDocumentPath());
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV Document", "*.csv"));
        return fileChooser;
    }
}
