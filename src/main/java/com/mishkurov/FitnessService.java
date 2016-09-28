package com.mishkurov;
/**
 * @author Anton_Mishkurov
 */
public class FitnessService {
    private double drinkTotal;
    private double caloriesTotal;

    public FitnessService() {
        drinkTotal = 0;
        caloriesTotal = 0;
    }

    public void drink(Double amount) {
        drinkTotal += amount;
    }

    public Double getDrinkAmount(){
        return drinkTotal;
    }

    public void eat(Double calories) {
        caloriesTotal+=calories;
    }

    public Double getCaloriesAmount(){
        return caloriesTotal;
    }
}
