package com.myproject;

import javafx.beans.property.SimpleStringProperty;

public class Person {

    private final String dateValue;
    private final String bodyWeight;
    private final String BMI;

    public Person(String dateValue, String bodyWeight, String BMI) {
        this.dateValue = dateValue;
        this.bodyWeight = bodyWeight;
        this.BMI = BMI;
    }

    public String getDateValue() {
        return dateValue;
    }

    public String getBodyWeight() {
        return bodyWeight;
    }

    public String getBMI() {
        return BMI;
    }




}
