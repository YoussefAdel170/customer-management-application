package com.yadel.customerclient.ui.dialogs;

import com.yadel.customerclient.model.Customer;
import javafx.scene.layout.VBox;

public class CustomerDialogContent {
    private final VBox root;
    private final CustomerForm form;
    private final CustomerButtonBar buttonBar;

    public CustomerDialogContent(Customer customer, boolean darkMode, java.util.function.Consumer<Customer> onSave) {
        this.form = new CustomerForm(customer);
        this.buttonBar = new CustomerButtonBar(darkMode, () -> {
            if (form.validate()) {
                onSave.accept(form.getCustomer());
            }
        });
        this.root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));
        root.getStyleClass().add("dialog-root");
        root.getChildren().addAll(form.getGrid(), buttonBar.getButtons());
    }

    public VBox getRoot() { return root; }
}