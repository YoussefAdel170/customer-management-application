package com.yadel.customerclient.ui;

import com.yadel.customerclient.ui.components.*;
import com.yadel.customerclient.ui.handlers.*;
import com.yadel.customerclient.ui.utils.KeyboardShortcutManager;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class MainView extends BorderPane {
    private final CustomerTable tableView;
    private final SearchBar searchBar;
    private final ButtonBar buttonBar;
    private final StatusBar statusBar;
    private final ThemeManager themeManager;
    private final CustomerLoader loader;
    private final CustomerCreator creator;
    private final CustomerUpdater updater;
    private final CustomerDeleter deleter;
    private final CustomerFilter filter;

    public MainView(Scene scene) {
        tableView = new CustomerTable();
        searchBar = new SearchBar();
        buttonBar = new ButtonBar(tableView);
        statusBar = new StatusBar();
        themeManager = new ThemeManager(scene);

        // Handlers
        loader = new CustomerLoader(tableView, statusBar, this::onCustomersLoaded, themeManager::isDarkMode);
        creator = new CustomerCreator(statusBar, this::reloadAfterChange, themeManager::isDarkMode);
        updater = new CustomerUpdater(statusBar, this::reloadAfterChange, themeManager::isDarkMode);
        deleter = new CustomerDeleter(statusBar, this::reloadAfterChange, themeManager::isDarkMode);
        filter = new CustomerFilter(tableView, statusBar);   // <-- filter now controls the table

        // Wire actions
        buttonBar.setOnAdd(creator::create);
        buttonBar.setOnEdit(() -> updater.update(tableView.getSelectionModel().getSelectedItem()));
        buttonBar.setOnDelete(() -> deleter.delete(tableView.getSelectionModel().getSelectedItem()));
        buttonBar.setOnRefresh(this::reload);
        buttonBar.setOnThemeToggle(() -> {
            themeManager.toggle();
            buttonBar.setThemeButtonText(themeManager.isDarkMode() ? "Light Mode" : "Dark Mode");
            statusBar.showTemporaryMessage(themeManager.isDarkMode() ? "Dark mode enabled" : "Light mode enabled", 2000);
        });

        searchBar.setOnSearch(filter::apply);   // <-- search triggers filter

        VBox topBox = new VBox(10, searchBar, buttonBar);
        topBox.setPadding(new Insets(10));
        setTop(topBox);
        setCenter(tableView);
        setBottom(statusBar);

        reload();

        KeyboardShortcutManager.setup(scene,
                creator::create,
                () -> updater.update(tableView.getSelectionModel().getSelectedItem()),
                this::reload,
                () -> searchBar.requestFocus(),
                () -> deleter.delete(tableView.getSelectionModel().getSelectedItem()),
                tableView.getSelectionModel().selectedItemProperty().isNotNull());
    }

    private void onCustomersLoaded(javafx.collections.ObservableList<com.yadel.customerclient.model.Customer> list) {
        filter.setSourceList(list);   // give the filter the full list
    }

    private void reload() {
        loader.load();
    }

    private void reloadAfterChange() {
        loader.load(); // reload after add/edit/delete
    }
}