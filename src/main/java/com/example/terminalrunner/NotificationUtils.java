package com.example.terminalrunner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class NotificationUtils {

    private static boolean isImage1Enabled = true;
    private static  boolean  applicationStatus = true;
    private static Runtime runtime = Runtime.getRuntime();
    private static Image resizedImage(Image image) {
        int trayIconWidth = 24;
        int trayIconHeight =20;
        Image resizeImage = image.getScaledInstance(trayIconWidth, trayIconHeight, Image.SCALE_SMOOTH);
        return resizeImage;
    }
    public static void showNotification(String title, String message) throws IOException {
        try {
            runtime.exec("warp-cli connect");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            Image[] image1 = {Toolkit.getDefaultToolkit().getImage(NotificationUtils.class.getResource("/one.png"))};
            image1[0] = resizedImage(image1[0]);
            TrayIcon trayIcon = new TrayIcon(image1[0]);

            Image[] image2 = {Toolkit.getDefaultToolkit().getImage(NotificationUtils.class.getResource("/two.png"))};
            image2[0] = resizedImage(image2[0]);

//            trayIcon.setImageAutoSize(true);
//            trayIcon.setBounds(trayIcon.getBounds().x, trayIcon.getBounds().y + verticalShift,
//                    trayIcon.getIconWidth(), trayIcon.getIconHeight());
//            trayIcon.se
            PopupMenu popupMenu = new PopupMenu();
            MenuItem exitMenuItem = new MenuItem("Exit");

            // Add an ActionListener to handle the exit action
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
                        if (isImage1Enabled) {
                            trayIcon.setImage(image2[0]);
                            isImage1Enabled = false;
                        } else {
                            trayIcon.setImage(image1[0]);
                            isImage1Enabled = true;
                        }
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
    private static File chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Image File");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    private static BufferedImage loadImageFromFile(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static  void runInTerminal(){
        try {
            if (!applicationStatus) {
                runtime.exec("warp-cli connect");
                applicationStatus = true;

            } else {
                runtime.exec("warp-cli disconnect");
                applicationStatus = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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