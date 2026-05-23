package com.yadel.customerclient.ui.dialogs;

import com.yadel.customerclient.model.Customer;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Optional;

public class CustomerDialog {
    private final Customer customer;
    private final Stage stage;
    private boolean confirmed = false;

    public CustomerDialog(Customer existing, boolean darkMode) {
        this.customer = existing != null ? existing : new Customer();
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(existing == null ? "Add Customer" : "Edit Customer");

        VBox root = new CustomerDialogContent(customer, darkMode, this::onConfirm).getRoot();
        Scene scene = new Scene(root, 480, 300);
        String cssPath = "/com/yadel/customerclient/ui/styles.css";
        var cssUrl = getClass().getResource(cssPath);
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
        if (darkMode) root.getStyleClass().add("dark");

        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void onConfirm(Customer updated) {
        customer.setName(updated.getName());
        customer.setEmail(updated.getEmail());
        customer.setPhone(updated.getPhone());
        confirmed = true;
        stage.close();
    }

    public Optional<Customer> showAndWait() {
        stage.showAndWait();
        return confirmed ? Optional.of(customer) : Optional.empty();
    }
}