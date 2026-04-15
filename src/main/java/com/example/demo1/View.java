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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        gridPane.getStyleClass().add("grid-pane");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);
        }

        historyField = new TextField("");
        historyField.setAlignment(Pos.CENTER_RIGHT);
        historyField.setPrefHeight(30);
        historyField.setEditable(false);
        historyField.setFocusTraversable(false);
        historyField.getStyleClass().add("history-field");
        historyField.setMaxWidth(Double.MAX_VALUE);

        textField = new TextField("");
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.setPrefHeight(60);
        textField.setMaxWidth(Double.MAX_VALUE);
        textField.setEditable(false);

        textField.getStyleClass().add("form-control");

        StackPane historyPane = new StackPane(historyField);

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
        stage.setTitle("RETRO CALC");
        stage.setScene(scene);
        stage.setResizable(false);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            if (code.isDigitKey()) {
                controller.processNumbers(event.getText());
            } else if (code == KeyCode.DECIMAL || code == KeyCode.PERIOD) {
                controller.processNumbers(".");
            } else if (code == KeyCode.ADD || (code == KeyCode.EQUALS && event.isShiftDown())) {
                controller.processOperators("+");
            } else if (code == KeyCode.SUBTRACT || code == KeyCode.MINUS) {
                controller.processOperators("-");
            } else if (code == KeyCode.MULTIPLY || (code == KeyCode.DIGIT8 && event.isShiftDown())) {
                controller.processOperators("*");
            } else if (code == KeyCode.DIVIDE || code == KeyCode.SLASH) {
                controller.processOperators("/");
            } else if (code == KeyCode.ENTER || code == KeyCode.EQUALS) {
                controller.processOperators("=");
            } else if (code == KeyCode.BACK_SPACE || code == KeyCode.DELETE || code == KeyCode.ESCAPE) {
                controller.clear();
            }
        });

        stage.show();

        showStartupAnimation();
    }

    private void showStartupAnimation() {
        Timeline startupTimeline = new Timeline();

        String retroCalc = "RETRO CALC";
        for (int i = 0; i <= retroCalc.length(); i++) {
            final int index = i;
            startupTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 50), e -> {
                textField.setText(retroCalc.substring(0, index));
            }));
        }

        double startBooting = retroCalc.length() * 50 + 200;
        String booting = "BOOTING";

        for (int i = 0; i <= booting.length(); i++) {
            final int index = i;
            startupTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(startBooting + i * 50), e -> {
                textField.setText(booting.substring(0, index));
            }));
        }

        double dotsStart = startBooting + booting.length() * 50 + 100;
        for (int i = 1; i <= 3; i++) {
            final int dotCount = i;
            startupTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(dotsStart + i * 300), e -> {
                textField.setText(booting + ".".repeat(dotCount));
            }));
        }

        startupTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(dotsStart + 1200), e -> {
            textField.setText("0");
            controller.clear();
        }));

        startupTimeline.play();
    }

    public void updateDisplay(String mainText, String historyText) {
        updateDisplay(mainText, historyText, false);
    }

    public void updateDisplay(String mainText, String historyText, boolean isClear) {
        if (isClear) {
            animateClear(textField);
            animateClear(historyField);
        } else {
            animateText(textField, mainText, true);
            animateText(historyField, historyText, false);
        }

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
        scanlinePane.setPrefSize(338, 90);

        Rectangle scanline = new Rectangle(0, 0, 350, 10);
        scanline.setOpacity(0.15);
        scanline.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.2, Color.web("#33ff33", 0.3)),
                new Stop(0.5, Color.LIME),
                new Stop(0.8, Color.web("#33ff33", 0.3)),
                new Stop(1, Color.TRANSPARENT)));

        Rectangle hazyLayer = new Rectangle(338, 100);
        hazyLayer.setFill(Color.web("#33ff33", 0.05));
        hazyLayer.setMouseTransparent(true);

        scanlinePane.getChildren().addAll(scanline, hazyLayer);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), scanline);
        transition.setFromY(-20);
        transition.setToY(110);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();

        StackPane displayStack = new StackPane(combinedDisplay, scanlinePane);
        displayStack.setClip(new Rectangle(338, 100));
        displayStack.setPrefWidth(338);
        displayStack.setMaxWidth(338);

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
        topBar.setPrefWidth(338);
        topBar.setMaxWidth(338);

        Button clearButton = new Button("AC");
        clearButton.setOnAction(event -> controller.clear());
        clearButton.getStyleClass().addAll("btn", "btn-main");
        clearButton.setPrefWidth(338);
        clearButton.setMaxWidth(338);

        VBox displayContainer = new VBox(topBar, displayStack, clearButton);
        displayContainer.setSpacing(5);
        VBox.setMargin(clearButton, new Insets(5, 0, 0, 0));
        displayContainer.setPrefWidth(338);
        displayContainer.setMaxWidth(338);
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

    private void animateClear(TextField field) {
        boolean isMain = (field == textField);
        if (isMain) {
            if (mainTimeline != null) mainTimeline.stop();
        } else {
            if (historyTimeline != null) historyTimeline.stop();
        }

        String currentText = field.getText();
        Timeline timeline = new Timeline();

        for (int i = 0; i <= currentText.length(); i++) {
            final int index = currentText.length() - i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 15), e -> {
                field.setText(currentText.substring(0, index));
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        if (isMain) mainTimeline = timeline;
        else historyTimeline = timeline;

        timeline.play();
    }
}
