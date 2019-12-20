package com.vv0z.currencywatcher.models;

public class Currency {

    private String unit;
    private Double value;

    public Currency(){

    }

    public Currency(String unit, Double value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
