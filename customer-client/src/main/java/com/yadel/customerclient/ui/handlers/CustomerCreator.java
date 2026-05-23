package com.yadel.customerclient.ui.handlers;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.service.CustomerApiService;
import com.yadel.customerclient.ui.components.StatusBar;
import com.yadel.customerclient.ui.dialogs.CustomerDialog;
import com.yadel.customerclient.ui.utils.DialogUtils;
import javafx.application.Platform;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class CustomerCreator {
    private final CustomerApiService apiService;
    private final StatusBar statusBar;
    private final Runnable onSuccess;
    private final BooleanSupplier darkModeSupplier;

    public CustomerCreator(StatusBar statusBar, Runnable onSuccess, BooleanSupplier darkModeSupplier) {
        this.apiService = new CustomerApiService();
        this.statusBar = statusBar;
        this.onSuccess = onSuccess;
        this.darkModeSupplier = darkModeSupplier;
    }

    public void create() {
        CustomerDialog dialog = new CustomerDialog(null, darkModeSupplier.getAsBoolean());
        Optional<Customer> result = dialog.showAndWait();
        result.ifPresent(customer -> {
            statusBar.showLoading(true, "Adding customer...");
            new Thread(() -> {
                try {
                    apiService.createCustomer(customer);
                    Platform.runLater(() -> {
                        if (onSuccess != null) onSuccess.run();
                        statusBar.showTemporaryMessage("Customer added successfully", 3000);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusBar.showLoading(false, "");
                        DialogUtils.showError("Add failed", e.getMessage(), darkModeSupplier.getAsBoolean());
                    });
                }
            }).start();
        });
    }
}