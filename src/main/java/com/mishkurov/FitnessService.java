package com.mishkurov;

public class FitnessService {
    private double drinkTotal;
    private double caloriesTotal;
    private double secondsTotal;
    private int pacesTotal;

    public FitnessService() {
        drinkTotal = 0;
        caloriesTotal = 0;
        secondsTotal = 0;
        pacesTotal = 0;
    }

    public void drink(Double amount) {
        drinkTotal += amount;
    }

    public Double getDrinkAmount() {
        return drinkTotal;
    }

    public void eat(Double calories) {
        caloriesTotal += calories;
    }

    public Double getCaloriesAmount() {
        return caloriesTotal;
    }

    public void move(double seconds) {
        secondsTotal += seconds;
    }

    public double getSecondsAmount() {
        return secondsTotal;
    }

    public void pace(int paces) {
        pacesTotal += paces;
    }

    public int getPacesAmount() {
        return pacesTotal;
    }
}
