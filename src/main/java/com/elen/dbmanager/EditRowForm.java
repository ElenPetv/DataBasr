package com.elen.dbmanager;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditRowForm extends VBox {

    public static void show(List<String> values, List<String> columns, OnSaveCallback onSaveCallback) {
        Scene scene = new Scene(new EditRowForm(values, columns, onSaveCallback), 500, 400);
        scene.getStylesheets().add(EditRowForm.class.getResource("stylesheet.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Добавление записи");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public EditRowForm(List<String> values, List<String> columns, OnSaveCallback onSaveCallback) {
        setAlignment(Pos.CENTER);
        setSpacing(8);
        getStyleClass().add("bg-color");
        List<StringProperty> valueProperties = new ArrayList<>(columns.size());
        ObservableList<Node> children = getChildren();
        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            TextArea textField = new TextArea(values == null ? "" : values.get(i));
            textField.setMaxWidth(250);
            textField.setPrefRowCount(1);
            valueProperties.add(textField.textProperty());
            Label label = new Label(column + ":");
            label.setWrapText(true);
            label.setMaxWidth(80);
            HBox field = new HBox(label, textField);
            field.setAlignment(Pos.BOTTOM_RIGHT);
            field.setMaxWidth(400);
            children.add(field);
        }

        Button buttonSave = new Button("Сохранить");
        VBox.setMargin(buttonSave, new Insets(32));
        buttonSave.setOnAction(event -> {

            List<String> newValues = valueProperties.stream()
                    .map(ObservableObjectValue::get)
                    .collect(Collectors.toList());

            onSaveCallback.onSave(newValues);
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
