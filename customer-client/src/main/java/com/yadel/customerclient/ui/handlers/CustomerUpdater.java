package com.yadel.customerclient.ui.handlers;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.service.CustomerApiService;
import com.yadel.customerclient.ui.components.StatusBar;
import com.yadel.customerclient.ui.dialogs.CustomerDialog;
import com.yadel.customerclient.ui.utils.DialogUtils;
import javafx.application.Platform;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class CustomerUpdater {
    private final CustomerApiService apiService;
    private final StatusBar statusBar;
    private final Runnable onSuccess;
    private final BooleanSupplier darkModeSupplier;

    public CustomerUpdater(StatusBar statusBar, Runnable onSuccess, BooleanSupplier darkModeSupplier) {
        this.apiService = new CustomerApiService();
        this.statusBar = statusBar;
        this.onSuccess = onSuccess;
        this.darkModeSupplier = darkModeSupplier;
    }

    public void update(Customer customer) {
        if (customer == null) {
            DialogUtils.showWarning("No Selection", "Please select a customer to edit.", darkModeSupplier.getAsBoolean());
            return;
        }
        CustomerDialog dialog = new CustomerDialog(customer, darkModeSupplier.getAsBoolean());
        Optional<Customer> result = dialog.showAndWait();
        result.ifPresent(updated -> {
            statusBar.showLoading(true, "Updating customer...");
            new Thread(() -> {
                try {
                    apiService.updateCustomer(updated.getId(), updated);
                    Platform.runLater(() -> {
                        if (onSuccess != null) onSuccess.run();
                        statusBar.showTemporaryMessage("Customer updated successfully", 3000);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusBar.showLoading(false, "");
                        DialogUtils.showError("Update failed", e.getMessage(), darkModeSupplier.getAsBoolean());
                    });
                }
            }).start();
        });
    }
}