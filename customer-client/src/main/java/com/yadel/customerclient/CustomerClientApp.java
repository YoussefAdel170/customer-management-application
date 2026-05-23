package com.yadel.customerclient;

import com.yadel.customerclient.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerClientApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new javafx.scene.layout.BorderPane(), 1000, 650); // temporary root
        MainView mainView = new MainView(scene);
        scene.setRoot(mainView);
        scene.getStylesheets().add(getClass().getResource("/com/yadel/customerclient/ui/styles.css").toExternalForm());
        primaryStage.setTitle("Customer Management - y.adel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}