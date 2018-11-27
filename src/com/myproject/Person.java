package com.myproject;

public class Person {

    private String dateValue;
    private String bodyWeight;
    private String BMI;

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

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    public void setBodyWeight(String bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }
}
