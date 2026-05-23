package com.yadel.customerclient.ui;

import com.yadel.customerclient.model.Customer;
import com.yadel.customerclient.service.CustomerApiService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * Main application window.
 * @author y.adel
 */
public class MainFrame extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;
    private final CustomerApiService apiService;

    public MainFrame() {
        apiService = new CustomerApiService();
        initUI();
        loadCustomers();
    }

    private void initUI() {
        setTitle("Customer Management - y.adel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        String[] columns = {"ID", "Name", "Email", "Phone", "Created At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        customerTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(rowSorter);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by name:"));
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
        searchPanel.add(searchField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addCustomer());
        editButton.addActionListener(e -> editCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        refreshButton.addActionListener(e -> loadCustomers());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void filter() {
        String text = searchField.getText();
        if (text.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
        }
    }

    private void loadCustomers() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                return apiService.getAllCustomers();
            }
            @Override
            protected void done() {
                try {
                    List<Customer> customers = get();
                    tableModel.setRowCount(0);
                    for (Customer c : customers) {
                        tableModel.addRow(new Object[]{
                                c.getId(), c.getName(), c.getEmail(),
                                c.getPhone(), c.getCreatedAt() != null ? c.getCreatedAt().replace("T", " ") : ""
                        });
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Failed to load customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }

    private void addCustomer() {
        CustomerDialog dialog = new CustomerDialog(this, "Add Customer", null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    apiService.createCustomer(dialog.getCustomer());
                    return null;
                }
                @Override
                protected void done() {
                    try {
                        get();
                        loadCustomers();
                        JOptionPane.showMessageDialog(MainFrame.this, "Customer added successfully");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Error adding customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            };
            worker.execute();
        }
    }

    private void editCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit");
            return;
        }
        int modelRow = customerTable.convertRowIndexToModel(selectedRow);
        Long id = (Long) tableModel.getValueAt(modelRow, 0);
        String name = (String) tableModel.getValueAt(modelRow, 1);
        String email = (String) tableModel.getValueAt(modelRow, 2);
        String phone = (String) tableModel.getValueAt(modelRow, 3);
        Customer customer = new Customer(id, name, email, phone, null);

        CustomerDialog dialog = new CustomerDialog(this, "Edit Customer", customer);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    apiService.updateCustomer(id, dialog.getCustomer());
                    return null;
                }
                @Override
                protected void done() {
                    try {
                        get();
                        loadCustomers();
                        JOptionPane.showMessageDialog(MainFrame.this, "Customer updated successfully");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Error updating customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            };
            worker.execute();
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete");
            return;
        }
        int modelRow = customerTable.convertRowIndexToModel(selectedRow);
        Long id = (Long) tableModel.getValueAt(modelRow, 0);
        String name = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete customer '" + name + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    apiService.deleteCustomer(id);
                    return null;
                }
                @Override
                protected void done() {
                    try {
                        get();
                        loadCustomers();
                        JOptionPane.showMessageDialog(MainFrame.this, "Customer deleted");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Error deleting customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            };
            worker.execute();
        }
    }
}