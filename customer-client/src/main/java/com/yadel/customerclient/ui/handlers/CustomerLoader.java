package com.yadel.customerclient.ui.handlers;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.service.CustomerApiService;
import com.yadel.customerclient.ui.components.CustomerTable;
import com.yadel.customerclient.ui.components.StatusBar;
import com.yadel.customerclient.ui.utils.DialogUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Consumer;

public class CustomerLoader {
    private final CustomerApiService apiService;
    private final CustomerTable tableView;
    private final StatusBar statusBar;
    private final Consumer<ObservableList<Customer>> onCustomersLoaded;
    private final java.util.function.BooleanSupplier darkModeSupplier;

    public CustomerLoader(CustomerTable tableView, StatusBar statusBar,
                          Consumer<ObservableList<Customer>> onCustomersLoaded,
                          java.util.function.BooleanSupplier darkModeSupplier) {
        this.apiService = new CustomerApiService();
        this.tableView = tableView;
        this.statusBar = statusBar;
        this.onCustomersLoaded = onCustomersLoaded;
        this.darkModeSupplier = darkModeSupplier;
    }

    public void load() {
        statusBar.showLoading(true, "Loading customers...");
        new Thread(() -> {
            try {
                List<Customer> customers = apiService.getAllCustomers();
                Platform.runLater(() -> {
                    ObservableList<Customer> list = FXCollections.observableArrayList(customers);
                    if (onCustomersLoaded != null) {
                        onCustomersLoaded.accept(list);  // this will set the filtered list as table items
                    }
                    statusBar.setText("Loaded " + customers.size() + " customers");
                    statusBar.showLoading(false, "");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusBar.showLoading(false, "");
                    DialogUtils.showError("Failed to load customers", e.getMessage(), darkModeSupplier.getAsBoolean());
                });
            }
        }).start();
    }
}