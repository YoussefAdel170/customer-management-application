package com.yadel.customerclient.ui.utils;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class KeyboardShortcutManager {

    public static void setup(Scene scene, Runnable onAdd, Runnable onEdit, Runnable onRefresh,
                             Runnable onSearchFocus, Runnable onDeleteIfSelected,
                             javafx.beans.value.ObservableValue<Boolean> hasSelection) {
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN), onAdd);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN), onEdit);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), onRefresh);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN), onSearchFocus);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE && hasSelection.getValue()) {
                onDeleteIfSelected.run();
            }
        });
    }
}