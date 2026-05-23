package com.yadel.customerclient.ui.handlers;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.ui.components.CustomerTable;
import com.yadel.customerclient.ui.components.StatusBar;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class CustomerFilter {
    private final CustomerTable tableView;
    private final StatusBar statusBar;
    private FilteredList<Customer> filteredList;

    public CustomerFilter(CustomerTable tableView, StatusBar statusBar) {
        this.tableView = tableView;
        this.statusBar = statusBar;
    }

    public void setSourceList(ObservableList<Customer> sourceList) {
        this.filteredList = new FilteredList<>(sourceList, customer -> true);
        tableView.setCustomers(filteredList);
    }

    public void apply(String text) {
        if (filteredList == null) return;
        if (text == null || text.trim().isEmpty()) {
            filteredList.setPredicate(c -> true);
        } else {
            String lower = text.toLowerCase();
            filteredList.setPredicate(c -> c.getName().toLowerCase().contains(lower));
        }
        int size = filteredList.size();
        statusBar.setText(size == 1 ? "Filtered to 1 customer" : "Filtered to " + size + " customers");
    }
}