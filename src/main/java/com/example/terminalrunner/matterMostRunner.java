package com.example.terminalrunner;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

public class matterMostRunner  extends Application{
    private static Boolean applicationStatus = false;

    private static Runtime runtime = Runtime.getRuntime();
    static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        matterMostIcon();
    }


    public static void main(String[] args) {
        launch();
    }

    public static void matterMostIcon() throws IOException, InterruptedException {

        if (SystemTray.isSupported()) {
            robot.keyPress(KeyEvent.VK_F11);
            Thread.sleep(2000); // Add a small delay
            robot.keyRelease(KeyEvent.VK_F11);
            // Simulate pressing the Super (Windows) key
            int clickX = 500; // X-coordinate
            int clickY = 300; // Y-coordinate

            // Move the mouse pointer to the desired coordinates
            robot.mouseMove(clickX, clickY);

            // Simulate a left mouse button click
            try {
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Pressing Super");
            robot.keyPress(KeyEvent.VK_META);
            Thread.sleep(100); // Add a small delay
            System.out.println("Pressing Right");
            robot.keyPress(KeyEvent.VK_RIGHT);
            Thread.sleep(100); // Add a small delay
            System.out.println("Releasing Right");
            robot.keyRelease(KeyEvent.VK_RIGHT);
            Thread.sleep(100); // Add a small delay
            System.out.println("Releasing Super");
            robot.keyRelease(KeyEvent.VK_META);

            SystemTray tray = SystemTray.getSystemTray();

            Image[] image1 = {Toolkit.getDefaultToolkit().getImage(NotificationUtils.class.getResource("/one.png"))};

            TrayIcon trayIcon = new TrayIcon(image1[0]);

            PopupMenu popupMenu = new PopupMenu();
            MenuItem exitMenuItem = new MenuItem("Exit");


            exitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // Exit the application
                }
            });

            popupMenu.add(exitMenuItem);
            trayIcon.setPopupMenu(popupMenu);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        runInTerminal();
                    }
              }
            });

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
                return;
            }



        } else {
            System.err.println("SystemTray is not supported.");
        }
    }


    public static void runScriptCmd(String scriptName) {
        try {
            // Get the input stream for the script resource
            InputStream scriptStream = MainRunner.class.getResourceAsStream("/" + scriptName);

            if (scriptStream == null) {
                System.err.println("Script not found: " + scriptName);
                return;
            }

            // Create a byte array to read the script content
            byte[] scriptBytes = scriptStream.readAllBytes();

            // Create a string from the script content
            String scriptContent = new String(scriptBytes);

            // Create a ProcessBuilder to run the script
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", scriptContent);

            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete (optional)
            int exitCode = process.waitFor();

            // Check the exit code
            if (exitCode == 0) {
                System.out.println("Script executed successfully");
            } else {
                System.err.println("Script execution failed with exit code: " + exitCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void runInTerminal() {
        try {
            if (!applicationStatus) {
                Process process = runtime.exec("mattermost-desktop");
                Thread.sleep(2000);
                runScriptCmd("shiftToLeft.cmd");

                // Simulate pressing the Super (Windows) key
                robot.keyPress(KeyEvent.VK_META);
                Thread.sleep(1000);
                robot.keyPress(KeyEvent.VK_UP);


                // Wait for a short delay
                Thread.sleep(1000);
                robot.keyRelease(KeyEvent.VK_META);
                // Simulate pressing the Up key

                robot.keyRelease(KeyEvent.VK_UP);
                System.out.println("Full screen");
                applicationStatus = true;
            } else {
                System.out.println("Minimized the application");

                // Simulate pressing the Super (Windows) key
                robot.keyPress(KeyEvent.VK_META);

                // Simulate pressing the H key
                robot.keyPress(KeyEvent.VK_H);

                // Release the keys
                robot.keyRelease(KeyEvent.VK_META);
                robot.keyRelease(KeyEvent.VK_H);

                applicationStatus = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
