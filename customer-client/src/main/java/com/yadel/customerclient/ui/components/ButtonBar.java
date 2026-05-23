package com.yadel.customerclient.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableView;
import com.yadel.customerclient.model.Customer;

public class ButtonBar extends HBox {
    private final Button addBtn;
    private final Button editBtn;
    private final Button deleteBtn;
    private final Button refreshBtn;
    private final Button themeToggleBtn;

    public ButtonBar(TableView<Customer> tableView) {
        setSpacing(10);
        setStyle("-fx-padding: 10;");

        addBtn = createButton("+ Add", "Add new customer (Ctrl+N)");
        editBtn = createButton("Edit", "Edit selected customer (Ctrl+E)");
        deleteBtn = createButton("Delete", "Delete selected customer (Delete key)");
        refreshBtn = createButton("Refresh", "Reload from server (Ctrl+R)");
        themeToggleBtn = createButton("Dark Mode", "Switch theme");

        deleteBtn.getStyleClass().add("danger-button");

        // Disable edit/delete when no row selected
        editBtn.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
        deleteBtn.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));

        getChildren().addAll(addBtn, editBtn, deleteBtn, refreshBtn, themeToggleBtn);
    }

    private Button createButton(String text, String tooltip) {
        Button btn = new Button(text);
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    // Event setters
    public void setOnAdd(Runnable action) { addBtn.setOnAction(e -> action.run()); }
    public void setOnEdit(Runnable action) { editBtn.setOnAction(e -> action.run()); }
    public void setOnDelete(Runnable action) { deleteBtn.setOnAction(e -> action.run()); }
    public void setOnRefresh(Runnable action) { refreshBtn.setOnAction(e -> action.run()); }
    public void setOnThemeToggle(Runnable action) { themeToggleBtn.setOnAction(e -> action.run()); }

    public void setThemeButtonText(String text) { themeToggleBtn.setText(text); }
}