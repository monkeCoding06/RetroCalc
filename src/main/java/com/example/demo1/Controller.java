package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    private final Model model;
    private TextField textField;
    private TextField historyField;
    private String operator = "";
    private boolean start = true;

    public Controller(Model model) {
        this.model = model;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public void setHistoryField(TextField historyField) {
        this.historyField = historyField;
    }

    public void processNumbers(ActionEvent event) {
        if (start) {
            textField.setText("");
            start = false;
        }

        String value = ((Button) event.getSource()).getText();
        textField.setText(textField.getText() + value);
    }

    public void processOperators(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();

        if (!"=".equals(value)) {
            if (!operator.isEmpty()) {
                return;
            }

            operator = value;
            double currentNumber = Double.parseDouble(textField.getText());
            model.setResult(currentNumber);
            historyField.setText(currentNumber + " " + operator);
            textField.setText("");
        } else {
            if (operator.isEmpty()) {
                return;
            }

            double currentNumber = Double.parseDouble(textField.getText());
            historyField.setText(historyField.getText() + " " + currentNumber + " =");
            double result = model.calculate(currentNumber, operator);
            textField.setText(String.valueOf(result));
            operator = "";
            start = true;
        }
    }

    public void clear(ActionEvent event) {
        textField.setText("");
        historyField.setText("");
        operator = "";
        start = true;
        model.setResult(0);
    }
}
