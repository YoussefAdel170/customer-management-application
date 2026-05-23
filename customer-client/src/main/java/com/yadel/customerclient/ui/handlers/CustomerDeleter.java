package com.yadel.customerclient.ui.handlers;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.service.CustomerApiService;
import com.yadel.customerclient.ui.components.StatusBar;
import com.yadel.customerclient.ui.dialogs.DeleteConfirmationDialog;
import com.yadel.customerclient.ui.utils.DialogUtils;
import javafx.application.Platform;

import java.util.function.BooleanSupplier;

public class CustomerDeleter {
    private final CustomerApiService apiService;
    private final StatusBar statusBar;
    private final Runnable onSuccess;
    private final BooleanSupplier darkModeSupplier;

    public CustomerDeleter(StatusBar statusBar, Runnable onSuccess, BooleanSupplier darkModeSupplier) {
        this.apiService = new CustomerApiService();
        this.statusBar = statusBar;
        this.onSuccess = onSuccess;
        this.darkModeSupplier = darkModeSupplier;
    }

    public void delete(Customer customer) {
        if (customer == null) {
            DialogUtils.showWarning("No Selection", "Please select a customer to delete.", darkModeSupplier.getAsBoolean());
            return;
        }
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(customer.getName(), darkModeSupplier.getAsBoolean());
        boolean confirmed = dialog.showAndWait();
        if (confirmed) {
            statusBar.showLoading(true, "Deleting customer...");
            new Thread(() -> {
                try {
                    apiService.deleteCustomer(customer.getId());
                    Platform.runLater(() -> {
                        if (onSuccess != null) onSuccess.run();
                        statusBar.showTemporaryMessage("Customer deleted successfully", 3000);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusBar.showLoading(false, "");
                        DialogUtils.showError("Delete failed", e.getMessage(), darkModeSupplier.getAsBoolean());
                    });
                }
            }).start();
        }
    }
}