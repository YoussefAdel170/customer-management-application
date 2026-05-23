package com.yadel.customerclient.ui;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.service.CustomerApiService;
import com.yadel.customerclient.ui.components.*;
import com.yadel.customerclient.ui.dialogs.CustomerDialog;
import com.yadel.customerclient.ui.dialogs.DeleteConfirmationDialog;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import java.util.List;
import java.util.Optional;

public class MainView extends BorderPane {
    private final CustomerApiService apiService;
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private FilteredList<Customer> filteredList;
    private final CustomerTable tableView;
    private final SearchBar searchBar;
    private final ButtonBar buttonBar;
    private final StatusBar statusBar;
    private final ThemeManager themeManager;

    public MainView(Scene scene) {
        apiService = new CustomerApiService();

        // Create components
        tableView = new CustomerTable();
        searchBar = new SearchBar();
        buttonBar = new ButtonBar(tableView);
        statusBar = new StatusBar();
        themeManager = new ThemeManager(scene);

        // Wire up event handlers
        buttonBar.setOnAdd(this::addCustomer);
        buttonBar.setOnEdit(this::editCustomer);
        buttonBar.setOnDelete(this::deleteCustomer);
        buttonBar.setOnRefresh(() -> loadCustomers());
        buttonBar.setOnThemeToggle(() -> {
            themeManager.toggle();
            buttonBar.setThemeButtonText(themeManager.isDarkMode() ? "Light Mode" : "Dark Mode");
            statusBar.showTemporaryMessage(themeManager.isDarkMode() ? "Dark mode enabled" : "Light mode enabled", 2000);
        });

        searchBar.setOnSearch(this::applyFilter);

        // Layout
        setTop(searchBar);
        setCenter(tableView);
        setBottom(statusBar);
        // Move button bar to top or wrap with search? Original had button bar below search.
        // We'll insert button bar between search and table.
        // Simpler: put both search and buttons in a VBox at top.
        javafx.scene.layout.VBox topBox = new javafx.scene.layout.VBox(10, searchBar, buttonBar);
        topBox.setPadding(new javafx.geometry.Insets(10));
        setTop(topBox);

        loadCustomers();
        setupKeyboardShortcuts(scene);
    }

    private void applyFilter(String text) {
        if (filteredList == null) return;
        if (text == null || text.trim().isEmpty()) {
            filteredList.setPredicate(c -> true);
        } else {
            String lower = text.toLowerCase();
            filteredList.setPredicate(c -> c.getName().toLowerCase().contains(lower));
        }
        statusBar.setText("Filtered to " + filteredList.size() + " customers");
    }

    private void loadCustomers() {
        statusBar.showLoading(true, "Loading customers...");
        new Thread(() -> {
            try {
                List<Customer> customers = apiService.getAllCustomers();
                Platform.runLater(() -> {
                    customerList.setAll(customers);
                    filteredList = new FilteredList<>(customerList, c -> true);
                    tableView.setCustomers(filteredList);
                    statusBar.setText("Loaded " + customerList.size() + " customers");
                    statusBar.showLoading(false, "");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusBar.showLoading(false, "");
                    showError("Failed to load customers", e.getMessage());
                });
            }
        }).start();
    }

    private void addCustomer() {
        CustomerDialog dialog = new CustomerDialog(null, themeManager.isDarkMode());
        Optional<Customer> result = dialog.showAndWait();
        result.ifPresent(customer -> {
            statusBar.showLoading(true, "Adding customer...");
            new Thread(() -> {
                try {
                    apiService.createCustomer(customer);
                    Platform.runLater(() -> {
                        loadCustomers();
                        statusBar.showTemporaryMessage("Customer added successfully", 3000);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusBar.showLoading(false, "");
                        showError("Add failed", e.getMessage());
                    });
                }
            }).start();
        });
    }

    private void editCustomer() {
        Customer selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a customer to edit.");
            return;
        }
        CustomerDialog dialog = new CustomerDialog(selected, themeManager.isDarkMode());
        Optional<Customer> result = dialog.showAndWait();
        result.ifPresent(updated -> {
            statusBar.showLoading(true, "Updating customer...");
            new Thread(() -> {
                try {
                    apiService.updateCustomer(updated.getId(), updated);
                    Platform.runLater(() -> {
                        loadCustomers();
                        statusBar.showTemporaryMessage("Customer updated successfully", 3000);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusBar.showLoading(false, "");
                        showError("Update failed", e.getMessage());
                    });
                }
            }).start();
        });
    }

    private void deleteCustomer() {
        Customer selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a customer to delete.");
            return;
        }
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(selected.getName(), themeManager.isDarkMode());
        boolean confirmed = dialog.showAndWait();
        if (confirmed) {
            statusBar.showLoading(true, "Deleting customer...");
            new Thread(() -> {
                try {
                    apiService.deleteCustomer(selected.getId());
                    Platform.runLater(() -> {
                        loadCustomers();
                        statusBar.showTemporaryMessage("Customer deleted successfully", 3000);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusBar.showLoading(false, "");
                        showError("Delete failed", e.getMessage());
                    });
                }
            }).start();
        }
    }
    private void setupKeyboardShortcuts(Scene scene) {
        scene.getAccelerators().put(
            new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.N, javafx.scene.input.KeyCombination.CONTROL_DOWN),
            () -> addCustomer());
        scene.getAccelerators().put(
            new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.E, javafx.scene.input.KeyCombination.CONTROL_DOWN),
            () -> editCustomer());
        scene.getAccelerators().put(
            new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.R, javafx.scene.input.KeyCombination.CONTROL_DOWN),
            () -> loadCustomers());
        scene.getAccelerators().put(
            new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.F, javafx.scene.input.KeyCombination.CONTROL_DOWN),
            () -> searchBar.requestFocus());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.DELETE && !tableView.getSelectionModel().isEmpty()) {
                deleteCustomer();
            }
        });
    }

    private void showError(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (themeManager.isDarkMode()) {
            alert.getDialogPane().getScene().getRoot().getStyleClass().add("dark");
        }
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (themeManager.isDarkMode()) {
            alert.getDialogPane().getScene().getRoot().getStyleClass().add("dark");
        }
        alert.showAndWait();
    }
}