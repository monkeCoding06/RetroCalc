package com.example.demo1;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class View {
    private TextField historyField;
    private TextField textField;
    private Controller controller;
    private Timeline mainTimeline;
    private Timeline historyTimeline;

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

        createScanLines(combinedDisplay, gridPane);

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
        animateText(textField, mainText, true);
        animateText(historyField, historyText, false);

        if ("DIVISION BY ZERO".equals(mainText)) {
            if (!textField.getStyleClass().contains("text-field-error")) {
                textField.getStyleClass().add("text-field-error");
            }
        } else {
            textField.getStyleClass().remove("text-field-error");
        }
    }

    // this method is just for the scanlines
    private void createScanLines(VBox combinedDisplay, GridPane gridPane){
        Pane scanlinePane = new Pane();
        scanlinePane.setMouseTransparent(true);
        scanlinePane.setPrefSize(335, 90);

        Rectangle scanline = new Rectangle(0, 0, 350, 10);
        scanline.setOpacity(0.15);
        scanline.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.2, Color.web("#33ff33", 0.3)),
                new Stop(0.5, Color.LIME),
                new Stop(0.8, Color.web("#33ff33", 0.3)),
                new Stop(1, Color.TRANSPARENT)));

        Rectangle hazyLayer = new Rectangle(335, 100);
        hazyLayer.setFill(Color.web("#33ff33", 0.05));
        hazyLayer.setMouseTransparent(true);

        scanlinePane.getChildren().addAll(scanline, hazyLayer);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), scanline);
        transition.setFromY(-20);
        transition.setToY(110);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();

        StackPane displayStack = new StackPane(combinedDisplay, scanlinePane);
        displayStack.setClip(new Rectangle(335, 100));
        displayStack.setMaxWidth(335);

        Label logo = new Label("RETRO-CALC");
        logo.getStyleClass().add("calculator-logo");

        Circle onLed = new Circle(2);
        onLed.getStyleClass().add("on-led");

        StackPane ledSocket = new StackPane(onLed);
        ledSocket.getStyleClass().add("led-socket");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(logo, spacer, ledSocket);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 5, 2, 5));
        topBar.setMaxWidth(335);

        VBox displayContainer = new VBox(topBar, displayStack);
        displayContainer.setMaxWidth(335);
        displayContainer.setAlignment(Pos.CENTER);

        gridPane.add(displayContainer, 0, 0, 4, 1);
        GridPane.setHalignment(displayContainer, HPos.CENTER);
    }

    private void animateText(TextField field, String targetText, boolean isMain) {
        if (field.getText().equals(targetText)) return;

        if (isMain) {
            if (mainTimeline != null) mainTimeline.stop();
        } else {
            if (historyTimeline != null) historyTimeline.stop();
        }

        Timeline timeline = new Timeline();
        for (int i = 0; i <= targetText.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 20), e -> {
                field.setText(targetText.substring(0, index));
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        if (isMain) mainTimeline = timeline;
        else historyTimeline = timeline;

        timeline.play();
    }
}
