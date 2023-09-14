//package com.example.terminalrunner;
//
//import javafx.application.Application;
//
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.SnapshotParameters;
//import javafx.scene.image.Image;
//import javafx.scene.image.WritableImage;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class ImageConverter extends Application {
//
//    public static Image convertRootToImage(Node root, double width, double height) {
//        SnapshotParameters parameters = new SnapshotParameters();
//        parameters.setFill(Color.TRANSPARENT); // Optional: Set the background color to transparent if needed
//
//        WritableImage fxImage = new WritableImage((int) width, (int) height);
//        root.snapshot(parameters, fxImage);
//
//        return fxImage;
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Create the JavaFX root node with an orange background and white text
//        StackPane root = new StackPane();
//        root.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
//        root.getChildren().add(new javafx.scene.control.Label("Hello, World!"));
//
//        // Convert the root node to a JavaFX image
//        double width = 200; // Set your desired width
//        double height = 100; // Set your desired height
//        Image fxImage = convertRootToImage(root, width, height);
//
//        // Save the image as a temporary file
//        File imageFile;
//        try {
//            imageFile = File.createTempFile("hello_world", ".png");
//            saveImage(fxImage, imageFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        // Display the image in the system tray (Windows-only example)
//        if (SystemTray.isSupported()) {
//            SystemTray systemTray = SystemTray.getSystemTray();
//
//            try {
//                BufferedImage bufferedImage = ImageIO.read(imageFile);
//
//                // Create a Swing-based frame for displaying the image
//                JFrame frame = new JFrame("System Tray Image");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
//                JLabel label = new JLabel(new ImageIcon(bufferedImage));
//                frame.add(label);
//                frame.setVisible(true);
//
//                // Create a tray icon with an action to open the Swing frame
//                TrayIcon trayIcon = new TrayIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
//                trayIcon.addActionListener(e -> frame.setVisible(true));
//                systemTray.add(trayIcon);
//            } catch (AWTException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Show the JavaFX application window (optional)
//        Scene scene = new Scene(root, width, height);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Image Converter");
//        primaryStage.show();
//    }
//
//    public void saveImage(Image image, File file) {
//        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
//
//        try {
//            ImageIO.write(bufferedImage, "png", file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
