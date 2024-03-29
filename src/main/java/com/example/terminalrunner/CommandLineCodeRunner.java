package com.example.terminalrunner;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;
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
            java.awt.Image trayIconImage =  Toolkit.getDefaultToolkit().getImage(NotificationUtils.class.getResource("/commandLineRunner.png"));


            // Create a tray icon
            TrayIcon trayIcon = new TrayIcon(trayIconImage);
            trayIcon.setImageAutoSize(true);

            // Create a popup menu for the tray icon
            PopupMenu popupMenu = new PopupMenu();




            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.addActionListener(e -> System.exit(0));

            // Add menu items to the popup menu
            popupMenu.addSeparator();
            popupMenu.add(exitMenuItem);

            trayIcon.setPopupMenu(popupMenu);


            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
            trayIcon.addActionListener(e -> handleTrayIconClick(trayIcon));
        } else {
            System.err.println("SystemTray is not supported.");
        }

    }


    // Inject the JavaScriptBridge instance into the JavaScript context
//        webEngine.executeScript("window.javaBridge = {};");
//        webEngine.executeScript("window.javaBridge.addToDoItem = function(todoText) { javaBridge.addToDoItem(todoText); }");
//        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
//
//        });
    private static void showHtmlContent(TrayIcon trayIcon) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        webEngine.setJavaScriptEnabled(true);
        JSObject jsBridge = (JSObject) webView.getEngine().executeScript("window");
        jsBridge.setMember("javaBridge", new JavaScriptBridge(webView));

        webEngine.load(CommandLineCodeRunner.class.getResource("/commandLine.html").toExternalForm());

//        webEngine.WebConsoleListener.setDefa- ultListener(
//                (webView1, message, lineNumber, sourceId) -> {
//                    if (message.contains("Error")) {
//                        System.err.println("JavaScript Error: [" + sourceId + ":" + lineNumber + "] " + message);
//                    } else {
//                        System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
//                    }
//                }
//        );
        if(jsBridge.getMember("javaBridge") == null) {
            System.out.println("javaBridge not found");
        }else {
            System.out.println(jsBridge.getMember("javaBridge").toString());
        }

        // Create a JavaFX Stage to display the WebView
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(null); // Important: Set owner to null to remove taskbar entry

        Button closeButton = new Button("X");
        closeButton.setOnAction(e -> stage.close());

        // Create a VBox to hold the close button and WebView
        VBox root = new VBox();
        root.getChildren().addAll(closeButton, webView);

        // Set the scene with the VBox
        Scene scene = new Scene(root, 400, 300); // Adjust the size as needed
        stage.setScene(scene);

        // Position the stage below the system tray icon
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double x = trayIcon.getSize().getHeight() + 900; // Adjust the X-coordinate as needed
        double y = trayIcon.getSize().getWidth(); // Adjust the Y-coordinate as needed

        stage.setX(x);
        stage.setY(y);

        stage.show();


    }
    private static void handleTrayIconClick(TrayIcon trayIcon) {

        Platform.runLater(() -> {
            System.out.println("Tray icon clicked!");
            showHtmlContent(trayIcon);
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


 class JavaScriptBridge {
    private WebView webView;

    public JavaScriptBridge(WebView webView) {
        this.webView = webView;
    }

    public String addToDoItem(String todoText) {
        // Add your Java code logic here to handle the addition of the todo item.
        // You can use 'todoText' as the input parameter.
        // For example:
        // MyBackend.addToDoItem(todoText);
        System.out.println("fucitn back " + todoText);
        return "hello world";
    }
}


