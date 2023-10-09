package com.example.terminalrunner;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
public class matterMostRunner extends Application {
    private static Boolean applicationStatus = isMattermostRunning();
    private static Process matterMostProcess = null;
    private static String shiftToLeft= """
            SCREEN_WIDTH=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $3}')
            SCREEN_HEIGHT=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $4}')
            NEW_WIDTH=$((SCREEN_WIDTH / 2))
            NEW_HEIGHT=$SCREEN_HEIGHT
            wmctrl -r "Mattermost Desktop" -e 0,0,0,$NEW_WIDTH,$NEW_HEIGHT
            """;
    private static String shiftToRight= """
             SCREEN_WIDTH=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $3}')
             SCREEN_HEIGHT=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $4}')
             NEW_WIDTH=$((SCREEN_WIDTH / 2))
             NEW_HEIGHT=$SCREEN_HEIGHT
             wmctrl -r "Mattermost Desktop" -e 0,$((SCREEN_WIDTH - NEW_WIDTH)),0,$NEW_WIDTH,$NEW_HEIGHT
            """;
    private static String fullscren = """
           SCREEN_WIDTH=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $3}')
           SCREEN_HEIGHT=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $4}')
           TASKBAR_HEIGHT=30
           NEW_WIDTH=$SCREEN_WIDTH
           NEW_HEIGHT=$((SCREEN_HEIGHT - TASKBAR_HEIGHT))
           wmctrl -r "Mattermost Desktop" -e 0,0,$TASKBAR_HEIGHT,$NEW_WIDTH,$NEW_HEIGHT
            """;


    private static Runtime runtime = Runtime.getRuntime();

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        matterMostIcon();
    }


    public static void main(String[] args) {
//        launch();
        RecordSpeech recordSpeech = new RecordSpeech();
        recordSpeech.record();
    }

    private static Image resizedImage(Image image) {
        int trayIconWidth = 24;
        int trayIconHeight =24;
        Image resizeImage = image.getScaledInstance(trayIconWidth, trayIconHeight, Image.SCALE_SMOOTH);
        return resizeImage;
    }
    public static void matterMostIcon() throws IOException, InterruptedException {

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            Image[] image1 = {Toolkit.getDefaultToolkit().getImage(matterMostRunner.class.getResource("/chat1.png"))};
            image1[0] = resizedImage(image1[0]);
            TrayIcon trayIcon = new TrayIcon(image1[0]);


            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if (e.getClickCount() == 2) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            runInTerminal(shiftToLeft);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            runInTerminal(shiftToRight);
                        }
                    }
                    else if (SwingUtilities.isLeftMouseButton(e)) {
                        runInTerminal();
                    }
                    else if (SwingUtilities.isMiddleMouseButton(e)) {
                        runInTerminal(fullscren);
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




    private static void runInTerminal() {


        try {
            matterMostProcess = runtime.exec("mattermost-desktop");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        applicationStatus = true;


    }
    private static void runInTerminal(String script) {
       try {
           String[] cmd = { "bash", "-c", script };
           matterMostProcess = runtime.exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isMattermostRunning() {
        if (matterMostProcess == null) {
            return false; // Process not started
        }

        try {
            // Attempt to get the exit code of the process
            int exitCode = matterMostProcess.exitValue();
            return false; // Process has already exited
        } catch (IllegalThreadStateException e) {
            return true; // Process is still running
        }
    }
}
