package com.yadel.customerclient.ui.dialogs;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.ui.validation.EmailValidator;
import com.yadel.customerclient.ui.validation.NameValidator;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CustomerForm {
    private final Customer customer;
    private final TextField nameField, emailField, phoneField;
    private final Label nameError, emailError;
    private final GridPane grid;

    public CustomerForm(Customer customer) {
        this.customer = customer;
        nameField = new TextField(customer.getName() != null ? customer.getName() : "");
        emailField = new TextField(customer.getEmail() != null ? customer.getEmail() : "");
        phoneField = new TextField(customer.getPhone() != null ? customer.getPhone() : "");
        nameError = createErrorLabel();
        emailError = createErrorLabel();
        grid = buildGrid();
        attachValidation();
    }

    private Label createErrorLabel() {
        Label label = new Label();
        label.getStyleClass().add("field-error");
        label.setVisible(false);
        label.setManaged(false);
        return label;
    }

    private GridPane buildGrid() {
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(10));

        Label nameLabel = label("Full Name *");
        Label emailLabel = label("Email Address *");
        Label phoneLabel = label("Phone Number");

        nameField.setPromptText("Enter full name");
        emailField.setPromptText("user@example.com");
        phoneField.setPromptText("+1234567890");

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(nameError, 1, 1);
        form.add(emailLabel, 0, 2);
        form.add(emailField, 1, 2);
        form.add(emailError, 1, 3);
        form.add(phoneLabel, 0, 4);
        form.add(phoneField, 1, 4);
        return form;
    }

    private Label label(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("field-label");
        return lbl;
    }

    private void attachValidation() {
        nameField.textProperty().addListener((obs, old, newVal) -> validateName());
        emailField.textProperty().addListener((obs, old, newVal) -> validateEmail());
    }

    public boolean validate() {
        boolean nameOk = validateName();
        boolean emailOk = validateEmail();
        return nameOk && emailOk;
    }

    private boolean validateName() {
        var result = NameValidator.validate(nameField.getText());
        if (!result.isValid()) {
            nameError.setText("Warning: " + result.getErrorMessage());
            nameError.setVisible(true);
            nameError.setManaged(true);
            return false;
        }
        nameError.setVisible(false);
        nameError.setManaged(false);
        return true;
    }

    private boolean validateEmail() {
        var result = EmailValidator.validate(emailField.getText());
        if (!result.isValid()) {
            emailError.setText("Warning: " + result.getErrorMessage());
            emailError.setVisible(true);
            emailError.setManaged(true);
            return false;
        }
        emailError.setVisible(false);
        emailError.setManaged(false);
        return true;
    }

    public GridPane getGrid() { return grid; }

    public Customer getCustomer() {
        customer.setName(nameField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setPhone(phoneField.getText().trim());
        return customer;
    }
}