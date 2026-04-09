package com.example.demo1;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    private Model model;
    private View view;
    private Controller controller;

    @Override
    public void init() {
        model = new Model();
        controller = new Controller(model);
    }

    @Override
    public void start(Stage primaryStage) {
        view = new View();
        view.setController(controller);
        controller.setView(view);

        view.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
