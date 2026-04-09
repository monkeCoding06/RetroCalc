package com.example.demo1;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class View {
    private TextField historyField;
    private TextField textField;
    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void start(Stage stage) {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);
        }

        historyField = new TextField("");
        historyField.setAlignment(Pos.CENTER_RIGHT);
        historyField.setPrefHeight(30);
        historyField.setEditable(false);
        historyField.setFocusTraversable(false);
        historyField.getStyleClass().add("history-field");

        textField = new TextField("");
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.setPrefHeight(60);
        textField.setMaxWidth(Double.MAX_VALUE);
        textField.setEditable(false);

        textField.getStyleClass().add("form-control");

        Button clearButton = new Button("x");
        clearButton.setOnAction(event -> controller.clear());

        clearButton.getStyleClass().addAll("btn", "btn-danger");

        StackPane historyPane = new StackPane(historyField, clearButton);
        StackPane.setAlignment(clearButton, Pos.CENTER_LEFT);
        StackPane.setMargin(clearButton, new Insets(0, 0, 0, 5));

        VBox combinedDisplay = new VBox(historyPane, textField);
        combinedDisplay.setSpacing(0);
        combinedDisplay.getStyleClass().add("crt-screen");

        // Crt style scanlines kinda?
        Pane scanlinePane = new Pane();
        scanlinePane.setMouseTransparent(true);
        scanlinePane.setPrefSize(335, 90);

        Rectangle scanline = new Rectangle(0, 0, 350, 10);
        scanline.setOpacity(0.1);
        scanline.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.LIME),
                new Stop(1, Color.TRANSPARENT)));

        scanlinePane.getChildren().add(scanline);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), scanline);
        transition.setFromY(-20);
        transition.setToY(110);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();

        StackPane displayStack = new StackPane(combinedDisplay, scanlinePane);
        displayStack.setClip(new Rectangle(335, 100)); // Clip to display size
        displayStack.setMaxWidth(335);

        gridPane.add(displayStack, 0, 0, 4, 1);
        GridPane.setHalignment(displayStack, HPos.CENTER);
        // Crt style scanlines kinda?


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
                button.setPrefWidth(80);
                button.setPrefHeight(90);
                button.setMaxWidth(Double.MAX_VALUE);

                if ("=".equals(text)) {
                    button.getStyleClass().addAll("btn", "btn-success");
                    button.setOnAction(event -> controller.processOperators(text));
                } else if ("+-*/".contains(text)) {
                    button.getStyleClass().addAll("btn", "btn-primary");
                    button.setOnAction(event -> controller.processOperators(text));
                } else {
                    button.getStyleClass().addAll("btn", "btn-secondary");
                    button.setOnAction(event -> controller.processNumbers(text));
                }

                gridPane.add(button, col, row + 1);
                GridPane.setHalignment(button, HPos.CENTER);
                GridPane.setValignment(button, VPos.CENTER);
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

    public void updateDisplay(String mainText, String historyText) {
        textField.setText(mainText);
        historyField.setText(historyText);

        if ("DIVISION BY ZERO".equals(mainText)) {
            if (!textField.getStyleClass().contains("text-field-error")) {
                textField.getStyleClass().add("text-field-error");
            }
        } else {
            textField.getStyleClass().remove("text-field-error");
        }
    }
}
