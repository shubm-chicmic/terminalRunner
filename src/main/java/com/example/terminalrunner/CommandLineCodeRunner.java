package com.example.terminalrunner;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public  class CommandLineCodeRunner {
    private static boolean isImage1Enabled = true;
    private static Runtime runtime = Runtime.getRuntime();
//    private static Image resizedImage(Image image) {
//        int trayIconWidth = 24;
//        int trayIconHeight =20;
//        Image resizeImage = image.getScaledInstance(trayIconWidth, trayIconHeight, Image.SCALE_SMOOTH);
//        return resizeImage;
//    }
    public static void codeRunner(){

        if (SystemTray.isSupported()) {
            Platform.setImplicitExit(false); // Prevent JavaFX application from exiting when the window is closed

            // Create an image for the tray icon
            java.awt.Image trayIconImage =  Toolkit.getDefaultToolkit().getImage(NotificationUtils.class.getResource("/one.png"));


            // Create a tray icon
            TrayIcon trayIcon = new TrayIcon(trayIconImage);
            trayIcon.setImageAutoSize(true);

            // Create a popup menu for the tray icon
            PopupMenu popupMenu = new PopupMenu();



            // Create a menu item to exit the application
            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.addActionListener(e -> System.exit(0));

            // Add menu items to the popup menu
            popupMenu.addSeparator();
            popupMenu.add(exitMenuItem);

            trayIcon.setPopupMenu(popupMenu);

            // Add the tray icon to the system tray
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
            trayIcon.addActionListener(e -> handleTrayIconClick());
        } else {
            System.err.println("SystemTray is not supported.");
        }

    }
    private static void showHtmlContent() {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            webEngine.load(CommandLineCodeRunner.class.getResource("/commandLine.html").toExternalForm());

            // Create a JavaFX Stage to display the WebView
            Stage stage = new Stage();
            stage.setScene(new Scene(webView));
            stage.initStyle(StageStyle.UNDECORATED);

        Button closeButton = new Button("X");
        closeButton.setOnAction(e -> stage.close());

        // Create a VBox to hold the close button and WebView
        VBox root = new VBox();
        root.getChildren().addAll(closeButton, webView);

        // Set the scene with the VBox
        Scene scene = new Scene(root, 400, 300); // Adjust the size as needed
        stage.setScene(scene);

        // Position the close button at the top
        VBox.setMargin(closeButton, new Insets(10, 10, 0, 10));

        stage.show();


    }
    private static void handleTrayIconClick() {

        Platform.runLater(() -> {
            System.out.println("Tray icon clicked!");
            showHtmlContent();
        });
    }




    private static  void runInTerminal(java.util.List<String> listCommand){
        for (String command : listCommand){
            try {
                Process process = runtime.exec(command);
                int exitCode = process.waitFor(); // Wait for the command to complete
                if (exitCode != 0) {
                    System.err.println("Command failed with exit code:  " + exitCode);
                    System.err.println("Command : " + command);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
//     try {
//        Process process = runtime.exec(command);
//        int exitCode = process.waitFor(); // Wait for the command to complete
//        if (exitCode != 0) {
//            System.err.println("Command failed with exit code: " + exitCode);
//        }
//    } catch (IOException | InterruptedException e) {
//        throw new RuntimeException(e);
//    }
}