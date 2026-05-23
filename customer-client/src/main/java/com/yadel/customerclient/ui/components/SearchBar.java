package com.yadel.customerclient.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

public class SearchBar extends HBox {
    private final TextField searchField;
    private final Button clearBtn;
    private Consumer<String> onSearch;

    public SearchBar() {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(5);

        searchField = new TextField();
        searchField.setPromptText("Search by name...");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-field");

        clearBtn = new Button("X");
        clearBtn.getStyleClass().add("clear-search-btn");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            if (onSearch != null) onSearch.accept("");
        });

        searchField.textProperty().addListener((obs, old, newVal) -> {
            if (onSearch != null) onSearch.accept(newVal);
        });

        getChildren().addAll(searchField, clearBtn);
    }

    public void setOnSearch(Consumer<String> callback) {
        this.onSearch = callback;
    }

    public void clear() {
        searchField.clear();
    }

    public String getText() {
        return searchField.getText();
    }
}