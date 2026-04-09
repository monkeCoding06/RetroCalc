package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class View extends Application {
    @Override
    public void start(Stage stage) {

        Model model = new Model();
        Controller controller = new Controller(model);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        TextField historyField = new TextField("");
        historyField.setAlignment(Pos.CENTER_RIGHT);
        historyField.setPrefHeight(30);
        historyField.setEditable(false);
        historyField.setFocusTraversable(false);
        historyField.getStyleClass().add("history-field");

        TextField textField = new TextField("");
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.setPrefHeight(60);
        textField.setMaxWidth(Double.MAX_VALUE);
        textField.setEditable(false);

        controller.setTextField(textField);
        controller.setHistoryField(historyField);

        textField.getStyleClass().add("form-control");

        Button clearButton = new Button("x");
        clearButton.setOnAction(controller::clear);

        clearButton.getStyleClass().addAll("btn", "btn-danger");

        StackPane historyPane = new StackPane(historyField, clearButton);
        StackPane.setAlignment(clearButton, Pos.CENTER_LEFT);
        StackPane.setMargin(clearButton, new Insets(0, 0, 0, 5));

        VBox combinedDisplay = new VBox(historyPane, textField);
        combinedDisplay.setSpacing(0);

        gridPane.add(combinedDisplay, 0, 0, 4, 1);

        String[][] buttonLabels = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "+"},
                {"0", ".", "=", "-"}
        };

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                String text = buttonLabels[row][col];
                Button button = new Button(text);
                button.setPrefWidth(70);
                button.setPrefHeight(90);

                if ("=".equals(text)) {
                    button.getStyleClass().addAll("btn", "btn-success");
                    button.setOnAction(controller::processOperators);
                } else if ("+-*/".contains(text)) {
                    button.getStyleClass().addAll("btn", "btn-primary");
                    button.setOnAction(controller::processOperators);
                } else {
                    button.getStyleClass().addAll("btn", "btn-secondary");
                    button.setOnAction(controller::processNumbers);
                }

                gridPane.add(button, col, row + 1);
            }
        }

        Scene scene = new Scene(gridPane);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        var css = getClass().getResource("/style.css");


        scene.getStylesheets().add(css.toExternalForm());

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );
        stage.setTitle("Calc");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
