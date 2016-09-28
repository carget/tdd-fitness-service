package com.mishkurov;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FitnessService {
    public static final int PACES_PER_DAY = 2000;
    public static final double DRINK_PER_DAY = 1500.;
    public static final int MOVE_SECONDS_PER_DAY = 7200;
    public static final double CALORIES_PER_DAY = 2300.;
    private Map<LocalDate, Double> drinkTotal;
    private double caloriesTotal;
    private int secondsTotal;
    private int pacesTotal;

    public FitnessService() {
        drinkTotal = new HashMap<>();
        caloriesTotal = 0;
        secondsTotal = 0;
        pacesTotal = 0;
    }

    public void drink(LocalDate date, Double amount) {
        Double storedAmount = drinkTotal.get(date);
        storedAmount = storedAmount == null ? 0 : storedAmount;
        drinkTotal.put(date, storedAmount + amount);
    }

    public Double getDrinkAmount(LocalDate date) {
        Double drinkAmount = drinkTotal.get(date);
        return drinkAmount == null ? 0. : drinkAmount;
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

    public int getSecondsAmount() {
        return secondsTotal;
    }

    public void pace(int paces) {
        pacesTotal += paces;
    }

    public int getPacesAmount() {
        return pacesTotal;
    }

    public int getPacesLeft() {
        return PACES_PER_DAY - pacesTotal;
    }

    public double getDrinkLeft(LocalDate date) {
        return DRINK_PER_DAY - drinkTotal.get(date);
    }

    public int getMoveLeft() {
        return MOVE_SECONDS_PER_DAY - secondsTotal;
    }

    public double getCaloriesLeft() {
        return CALORIES_PER_DAY - caloriesTotal;
    }


}
