package com.yadel.customerclient.ui.utils;

import javafx.scene.control.Alert;

public class DialogUtils {

    public static void showError(String title, String message, boolean darkMode) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (darkMode) alert.getDialogPane().getScene().getRoot().getStyleClass().add("dark");
        alert.showAndWait();
    }

    public static void showWarning(String title, String message, boolean darkMode) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (darkMode) alert.getDialogPane().getScene().getRoot().getStyleClass().add("dark");
        alert.showAndWait();
    }
}