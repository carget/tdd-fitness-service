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
    private Map<LocalDate, Double> caloriesTotal;
    private Map<LocalDate, Integer> moveSecondsTotal;
    private Map<LocalDate, Integer> pacesTotal;

    public FitnessService() {
        drinkTotal = new HashMap<>();
        caloriesTotal = new HashMap<>();
        moveSecondsTotal = new HashMap<>();
        pacesTotal = new HashMap<>();
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

    public void eat(LocalDate date, Double calories) {
        Double consumedCalories = caloriesTotal.get(date);
        consumedCalories = consumedCalories == null ? 0 : consumedCalories;
        caloriesTotal.put(date, consumedCalories + calories);
    }

    public Double getCaloriesAmount(LocalDate date) {
        Double caloriesAmount = caloriesTotal.get(date);
        return caloriesAmount == null ? 0 : caloriesAmount;
    }

    public void move(LocalDate date, int seconds) {
        Integer moveSecondsRecorded = moveSecondsTotal.get(date);
        moveSecondsRecorded = moveSecondsRecorded == null ? 0 : moveSecondsRecorded;
        moveSecondsTotal.put(date, moveSecondsRecorded + seconds);
    }

    public int getMoveSecondsAmount(LocalDate date) {
        Integer moveSecondAmount = moveSecondsTotal.get(date);
        moveSecondAmount = moveSecondAmount == null ? 0 : moveSecondAmount;
        return moveSecondAmount;
    }

    public void pace(LocalDate date, int paces) {
        Integer pacesRecorded = pacesTotal.get(date);
        pacesRecorded = pacesRecorded == null ? 0 : pacesRecorded;
        pacesTotal.put(date, pacesRecorded + paces);
    }

    public int getPacesAmount(LocalDate date) {
        Integer pacesAmount = pacesTotal.get(date);
        return pacesAmount == null ? 0 : pacesAmount;
    }

    public int getPacesLeft(LocalDate date) {
        Integer pacesRecorded = pacesTotal.get(date);
        pacesRecorded = pacesRecorded == null ? 0 : pacesRecorded;
        return PACES_PER_DAY - pacesRecorded;
    }

    public double getDrinkLeft(LocalDate date) {
        Double consumedDrink = drinkTotal.get(date);
        consumedDrink = consumedDrink == null ? 0 : consumedDrink;
        return DRINK_PER_DAY - consumedDrink;
    }

    public int getMoveSecondsLeft(LocalDate date) {
        Integer moveSecondsRecorded = moveSecondsTotal.get(date);
        moveSecondsRecorded = moveSecondsRecorded == null ? 0 : moveSecondsRecorded;
        return MOVE_SECONDS_PER_DAY - moveSecondsRecorded;
    }

    public double getCaloriesLeft(LocalDate date) {
        Double consumedCalories = caloriesTotal.get(date);
        consumedCalories = consumedCalories == null ? 0 : consumedCalories;
        return CALORIES_PER_DAY - consumedCalories;
    }


}
