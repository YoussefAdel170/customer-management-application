package com.yadel.customerclient.ui.components;

import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class ThemeManager {
    private boolean darkMode = false;
    private Scene scene;
    private Runnable onThemeChanged;

    public ThemeManager(Scene scene) {
        this.scene = scene;
    }

    public void setOnThemeChanged(Runnable callback) {
        this.onThemeChanged = callback;
    }

    public boolean isDarkMode() { return darkMode; }

    public void toggle() {
        darkMode = !darkMode;
        if (darkMode) {
            scene.getRoot().getStyleClass().add("dark");
        } else {
            scene.getRoot().getStyleClass().remove("dark");
        }
        FadeTransition fade = new FadeTransition(Duration.millis(300), scene.getRoot());
        fade.setFromValue(0.8);
        fade.setToValue(1.0);
        fade.play();
        if (onThemeChanged != null) onThemeChanged.run();
    }
}