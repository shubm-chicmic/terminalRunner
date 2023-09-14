package com.example.terminalrunner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UbuntuStyleContextMenu extends Application {

    // Inside your start method
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add("styles.css"); // Load your CSS for styling
        primaryStage.initStyle(StageStyle.UNDECORATED);
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menu-bar"); // Apply the menu bar style

        VBox menuContent = new VBox();
        menuContent.getStyleClass().add("menu-content"); // Apply the menu content style

        Menu settingsMenu = new Menu("Settings");
        settingsMenu.getStyleClass().add("menu"); // Apply the menu style

        MenuItem setting1 = new MenuItem("Setting 1");
        MenuItem setting2 = new MenuItem("Setting 2");
        // Add more menu items

        settingsMenu.getItems().addAll(setting1, setting2);

        menuBar.getMenus().add(settingsMenu); // Add settingsMenu to menuBar

        menuContent.getChildren().addAll(menuBar); // Add menuBar to menuContent

        VBox menuBox = new VBox(menuContent);
        menuBox.getStyleClass().add("menu-box"); // Apply the menu box position style

        root.getChildren().add(menuBox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ubuntu Style Settings");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
