package com.yadel.customerclient.ui.components;

import com.yadel.customerclient.model.Customer;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerTable extends TableView<Customer> {
    public CustomerTable() {
        setPlaceholder(new javafx.scene.control.Label("No customers yet. Click 'Add' to create one."));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Customer, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(250);

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(150);

        TableColumn<Customer, String> createdAtCol = new TableColumn<>("Created At");
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        createdAtCol.setPrefWidth(180);

        getColumns().addAll(idCol, nameCol, emailCol, phoneCol, createdAtCol);
    }

    public void setCustomers(ObservableList<Customer> customers) {
        setItems(customers);
    }
}