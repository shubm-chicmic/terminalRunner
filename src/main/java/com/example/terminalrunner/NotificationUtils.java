package com.example.terminalrunner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NotificationUtils {
    private static boolean isImage1Enabled = true;
    private static boolean applicationStatus = true;
    private static Runtime runtime = Runtime.getRuntime();

    public NotificationUtils() {
    }

    private static Image resizedImage(Image image) {
        int trayIconWidth = 24;
        int trayIconHeight = 20;
        Image resizeImage = image.getScaledInstance(trayIconWidth, trayIconHeight, 4);
        return resizeImage;
    }

    public static void showNotification(String title, String message) throws IOException {
        try {
            runtime.exec("warp-cli connect");
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image[] image1 = new Image[]{Toolkit.getDefaultToolkit().getImage(NotificationUtils.class.getResource("/one.png"))};
            image1[0] = resizedImage(image1[0]);
            TrayIcon trayIcon = new TrayIcon(image1[0]);
            Image[] image2 = new Image[]{Toolkit.getDefaultToolkit().getImage(NotificationUtils.class.getResource("/two.png"))};
            image2[0] = resizedImage(image2[0]);
//            trayIcon.addMouseListener(new 1(trayIcon, image2, image1));

            try {
                tray.add(trayIcon);
            } catch (AWTException var7) {
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
        int result = fileChooser.showOpenDialog((Component)null);
        return result == 0 ? fileChooser.getSelectedFile() : null;
    }

    private static BufferedImage loadImageFromFile(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static void runInTerminal() {
        try {
            if (!applicationStatus) {
                runtime.exec("warp-cli connect");
                applicationStatus = true;
            } else {
                runtime.exec("warp-cli disconnect");
                applicationStatus = false;
            }

        } catch (IOException var1) {
            throw new RuntimeException(var1);
        }
    }
}
