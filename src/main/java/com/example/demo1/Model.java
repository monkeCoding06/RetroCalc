package com.example.demo1;

public class Model {
    private double result = 0;
    private String operator = "";
    private String history = "";
    private String currentInput = "";

    public double calculate(double number, String operator) {
        switch (operator) {
            case "+":
                result += number;
                break;
            case "-":
                result -= number;
                break;
            case "*":
                result *= number;
                break;
            case "/":
                if (number != 0) {
                    result /= number;
                } else {
                    result = 0;
                }
                break;
            case "=":
                result = number;
                break;
        }
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public double getResult() {
        return result;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public void clear() {
        result = 0;
        operator = "";
        history = "";
        currentInput = "";
    }
}
