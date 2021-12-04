package com.elen.dbmanager;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditRowForm extends VBox {

    public static void show(List<String> values, List<String> columns, OnSaveCallback onSaveCallback) {
        Scene scene = new Scene(new EditRowForm(values, columns, onSaveCallback), 300, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public EditRowForm(List<String> values, List<String> columns, OnSaveCallback onSaveCallback) {
        List<StringProperty> valueProperties = new ArrayList<>(columns.size());
        ObservableList<Node> children = getChildren();
        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            TextField textField = new TextField(values == null ? "" : values.get(i));
            valueProperties.add(textField.textProperty());
            children.add(new HBox(new Label(column + ":"), textField));
        }

        Button buttonSave = new Button("Сохранить");
        buttonSave.setOnAction(event -> {
            onSaveCallback.onSave(valueProperties.stream()
                    .map(ObservableObjectValue::get)
                    .collect(Collectors.toList()));

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        children.add(buttonSave);
    }

    @FunctionalInterface
    public interface OnSaveCallback {
        void onSave(List<String> row);
    }
}
