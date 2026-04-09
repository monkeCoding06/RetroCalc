package com.example.demo1;

public class Controller {
    private final Model model;
    private View view;
    private boolean start = true;

    public Controller(Model model) {
        this.model = model;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void processNumbers(String value) {
        if (start || model.getCurrentInput().equals("DIVISION BY ZERO")) {
            model.setCurrentInput("");
            start = false;
        }

        model.setCurrentInput(model.getCurrentInput() + value);
        view.updateDisplay(model.getCurrentInput(), model.getHistory());
    }

    public void processOperators(String value) {
        if (model.getCurrentInput().equals("DIVISION BY ZERO")) {
            return;
        }
        if (!"=".equals(value)) {
            if (!model.getOperator().isEmpty()) {
                return;
            }

            model.setOperator(value);
            double currentNumber = Double.parseDouble(model.getCurrentInput());
            model.setResult(currentNumber);
            model.setHistory(currentNumber + " " + model.getOperator());
            model.setCurrentInput("");
            view.updateDisplay(model.getCurrentInput(), model.getHistory());
        } else {
            if (model.getOperator().isEmpty()) {
                return;
            }

            double currentNumber = Double.parseDouble(model.getCurrentInput());
            model.setHistory(model.getHistory() + " " + currentNumber + " =");
            double result = model.calculate(currentNumber, model.getOperator());
            if (Double.isNaN(result)) {
                model.setCurrentInput("DIVISION BY ZERO");
            } else {
                model.setCurrentInput(String.valueOf(result));
            }
            model.setOperator("");
            start = true;
            view.updateDisplay(model.getCurrentInput(), model.getHistory());
        }
    }

    public void clear() {
        model.clear();
        start = true;
        view.updateDisplay("", "", true);
    }
}
