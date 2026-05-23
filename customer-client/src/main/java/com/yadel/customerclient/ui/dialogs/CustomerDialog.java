package com.yadel.customerclient.ui.dialogs;

import com.yadel.customerclient.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomerDialog {

    private final Customer customer;
    private final Stage stage;
    private final boolean darkMode;

    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;

    private Label nameError;
    private Label emailError;

    private boolean confirmed = false;

    public CustomerDialog(Customer existing, boolean darkMode) {

        this.customer = (existing != null)
                ? existing
                : new Customer();

        this.darkMode = darkMode;

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle(
                existing == null
                        ? "Add Customer"
                        : "Edit Customer"
        );

        createUI();
    }

    private void createUI() {

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("dialog-root");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(10));

        // =========================
        // Name
        // =========================

        Label nameLabel = new Label("Full Name *");
        nameLabel.getStyleClass().add("field-label");

        nameField = new TextField(
                customer.getName() != null
                        ? customer.getName()
                        : ""
        );

        nameField.setPromptText("Enter full name");

        nameError = new Label();
        nameError.getStyleClass().add("field-error");
        nameError.setVisible(false);
        nameError.setManaged(false);

        // =========================
        // Email
        // =========================

        Label emailLabel = new Label("Email Address *");
        emailLabel.getStyleClass().add("field-label");

        emailField = new TextField(
                customer.getEmail() != null
                        ? customer.getEmail()
                        : ""
        );

        emailField.setPromptText("user@example.com");

        emailError = new Label();
        emailError.getStyleClass().add("field-error");
        emailError.setVisible(false);
        emailError.setManaged(false);

        // =========================
        // Phone
        // =========================

        Label phoneLabel = new Label("Phone Number");
        phoneLabel.getStyleClass().add("field-label");

        phoneField = new TextField(
                customer.getPhone() != null
                        ? customer.getPhone()
                        : ""
        );

        phoneField.setPromptText("+1234567890");

        // =========================
        // Form Layout
        // =========================

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(nameError, 1, 1);

        form.add(emailLabel, 0, 2);
        form.add(emailField, 1, 2);
        form.add(emailError, 1, 3);

        form.add(phoneLabel, 0, 4);
        form.add(phoneField, 1, 4);

        // =========================
        // Validation Listeners
        // =========================

        nameField.textProperty().addListener(
                (obs, oldVal, newVal) -> validateName()
        );

        emailField.textProperty().addListener(
                (obs, oldVal, newVal) -> validateEmail()
        );

        // =========================
        // Buttons
        // =========================

        Button saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);
        saveBtn.getStyleClass().add("save-button");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setCancelButton(true);

        saveBtn.setOnAction(e -> {

            boolean validName = validateName();
            boolean validEmail = validateEmail();

            if (validName && validEmail) {

                saveData();

                confirmed = true;

                stage.close();
            }
        });

        cancelBtn.setOnAction(e -> stage.close());

        HBox buttons = new HBox(15, saveBtn, cancelBtn);

        buttons.setAlignment(Pos.CENTER_RIGHT);

        buttons.setPadding(
                new Insets(15, 0, 0, 0)
        );

        root.getChildren().addAll(form, buttons);

        // =========================
        // Scene
        // =========================

        Scene scene = new Scene(root, 480, 300);

        String cssPath =
                "/com/yadel/customerclient/ui/styles.css";

        var cssUrl = getClass().getResource(cssPath);

        if (cssUrl != null) {
            scene.getStylesheets().add(
                    cssUrl.toExternalForm()
            );
        }

        if (darkMode) {
            root.getStyleClass().add("dark");
        }

        stage.setScene(scene);

        stage.setResizable(false);
    }

    private boolean validateName() {

        String name = nameField.getText().trim();

        if (name.isEmpty()) {

            showError(
                    nameError,
                    "Name is required"
            );

            return false;
        }

        hideError(nameError);

        return true;
    }

    private boolean validateEmail() {

        String email = emailField.getText().trim();

        if (email.isEmpty()) {

            showError(
                    emailError,
                    "Email is required"
            );

            return false;
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@(.+)$"
        )) {

            showError(
                    emailError,
                    "Invalid email format"
            );

            return false;
        }

        hideError(emailError);

        return true;
    }

    private void showError(
            Label label,
            String message
    ) {

        label.setText(
                "Error: " + message
        );

        label.setVisible(true);

        label.setManaged(true);
    }

    private void hideError(Label label) {

        label.setVisible(false);

        label.setManaged(false);
    }

    private void saveData() {

        customer.setName(
                nameField.getText().trim()
        );

        customer.setEmail(
                emailField.getText().trim()
        );

        customer.setPhone(
                phoneField.getText().trim()
        );
    }

    public Optional<Customer> showAndWait() {

        stage.showAndWait();

        return confirmed
                ? Optional.of(customer)
                : Optional.empty();
    }
}