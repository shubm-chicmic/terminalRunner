package com.example.terminalrunner;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class CommandLineCodeMainRunner extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        CommandLineCodeRunner.codeRunner();

    }

    public static void main(String[] args) {
        launch();
    }
}
