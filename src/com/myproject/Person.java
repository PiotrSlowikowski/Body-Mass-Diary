package com.myproject;

import javafx.beans.property.SimpleStringProperty;

public class Person {

    private final SimpleStringProperty dateValue;
    private final SimpleStringProperty bodyWeight;
    private final SimpleStringProperty email;

    public Person(String dValue, String bodyWeight, String email) {
        this.dateValue = new SimpleStringProperty(dValue);
        this.bodyWeight = new SimpleStringProperty(bodyWeight);
        this.email = new SimpleStringProperty(email);
    }

    public String getDateValue() {
        return dateValue.get();
    }

    public void setDateValue(String dValue) {
        dateValue.set(dValue);
    }

    public String getBodyWeight() {
        return bodyWeight.get();
    }

    public void setBodyWeight(String dValue) {
        bodyWeight.set(dValue);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String dValue) {
        email.set(dValue);
    }

}
