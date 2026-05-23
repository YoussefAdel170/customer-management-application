package com.yadel.customerclient.ui.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CustomerButtonBar {
    private final HBox buttonBar;

    public CustomerButtonBar(boolean darkMode, Runnable onSave) {
        Button saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
        saveBtn.getStyleClass().add("save-button");
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setCancelButton(true);
        cancelBtn.setOnAction(e -> cancelBtn.getScene().getWindow().hide());

        saveBtn.setOnAction(e -> onSave.run());

        buttonBar = new HBox(15, saveBtn, cancelBtn);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(15, 0, 0, 0));
    }

    public HBox getButtons() { return buttonBar; }
}