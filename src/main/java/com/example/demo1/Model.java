package com.example.demo1;

public class Model {
    private double result = 0;
    private String operator = "";
    private boolean start = true;

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
                if (number == 0) return 0;
                result /= number;
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
}
