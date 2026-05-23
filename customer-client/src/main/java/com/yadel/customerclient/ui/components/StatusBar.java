package com.yadel.customerclient.ui.components;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

public class StatusBar extends HBox {
    private final Label statusLabel;
    private final ProgressIndicator progressIndicator;

    public StatusBar() {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);
        setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
        getStyleClass().add("status-bar");

        statusLabel = new Label("Ready");
        statusLabel.getStyleClass().add("status-label");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(20, 20);

        getChildren().addAll(statusLabel, progressIndicator);
    }

    public void setText(String text) {
        Platform.runLater(() -> statusLabel.setText(text));
    }

    public void showLoading(boolean show, String message) {
        Platform.runLater(() -> {
            progressIndicator.setVisible(show);
            statusLabel.setText(show ? message : "Ready");
        });
    }

    public void showTemporaryMessage(String message, int milliseconds) {
        Platform.runLater(() -> {
            String old = statusLabel.getText();
            statusLabel.setText(message);
            new Thread(() -> {
                try { Thread.sleep(milliseconds); } catch (InterruptedException ignored) {}
                Platform.runLater(() -> {
                    if (statusLabel.getText().equals(message)) {
                        statusLabel.setText(old);
                    }
                });
            }).start();
        });
    }
}