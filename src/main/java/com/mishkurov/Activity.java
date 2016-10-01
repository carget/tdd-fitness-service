package com.mishkurov;

public enum Activity {

    EAT(1000.), DRINK(1000.), MOVE(1000.), PACE(1000.);

    private final double defaultValue;

    Activity(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

}
