package com.yadel.customerclient.ui.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DeleteConfirmationDialog {
    private final Stage stage;
    private boolean confirmed = false;

    public DeleteConfirmationDialog(String customerName, boolean darkMode) {
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Confirm Delete");

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("delete-dialog-root");

        // Warning icon – plain ASCII safe (no emoji)
        Label icon = new Label("!");
        icon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        icon.setStyle("-fx-text-fill: #dc2626;");

        Label titleLabel = new Label("Delete Customer");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #c02222;");
        titleLabel.getStyleClass().add("dialog-title");

        // Customer name – separate Text node with its own style class
        Text nameText = new Text(customerName);
        nameText.getStyleClass().add("delete-customer-name");
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label warningLabel = new Label("This action cannot be undone.");
        warningLabel.setStyle("-fx-text-fill: #f97316; -fx-font-size: 12px;");

        Button confirmBtn = new Button("Yes, Delete");
        confirmBtn.getStyleClass().add("danger-button");
        confirmBtn.setPrefWidth(120);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(120);
        cancelBtn.getStyleClass().add("cancel-button");

        confirmBtn.setOnAction(e -> {
            confirmed = true;
            stage.close();
        });
        cancelBtn.setOnAction(e -> stage.close());

        HBox buttonBar = new HBox(15, confirmBtn, cancelBtn);
        buttonBar.setAlignment(Pos.CENTER);

        root.getChildren().addAll(icon, titleLabel, nameText, warningLabel, buttonBar);

        Scene scene = new Scene(root, 450, 280);
        String cssPath = "/com/yadel/customerclient/ui/styles.css";
        var cssUrl = getClass().getResource(cssPath);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("CSS not found: " + cssPath);
        }
        if (darkMode) {
            root.getStyleClass().add("dark");
        }
        stage.setScene(scene);
        stage.setResizable(false);
    }

    public boolean showAndWait() {
        stage.showAndWait();
        return confirmed;
    }
}