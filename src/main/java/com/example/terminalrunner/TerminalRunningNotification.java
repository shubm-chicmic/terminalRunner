package com.example.terminalrunner;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TerminalRunningNotification extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        NotificationUtils.showNotification("", "");

    }

    public static void main(String[] args) {
        launch();
    }
}