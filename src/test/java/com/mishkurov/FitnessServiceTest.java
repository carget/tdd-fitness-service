package com.mishkurov;

import org.hamcrest.number.IsCloseTo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static com.mishkurov.FitnessService.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

public class FitnessServiceTest {

    public static final double EPSILON = 1E-6;
    private FitnessService service;

    @Before
    public void setUp() throws Exception {
        service = new FitnessService();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void drink() {
        Double amount = 100.;
        service.drink(LocalDate.parse("2016-09-27"), amount);
        service.drink(LocalDate.parse("2016-09-28"), amount);
        service.drink(LocalDate.parse("2016-09-28"), amount);
        service.drink(LocalDate.parse("2016-09-29"), amount);
        assertThat(service.getDrinkAmount(LocalDate.parse("2016-09-28")), closeTo(amount * 2., EPSILON));
        assertThat(service.getDrinkAmount(LocalDate.parse("2016-09-27")), closeTo(amount * 1., EPSILON));
        assertThat(service.getDrinkAmount(LocalDate.parse("2016-09-26")), closeTo(amount * 0., EPSILON));
    }

    @Test
    public void eat() {
        Double calories = 15.;
        service.eat(calories);
        service.eat(calories);
        assertThat(service.getCaloriesAmount(), is(calories * 2));
    }

    @Test
    public void move() {
        int seconds = 3600;
        service.move(seconds);
        service.move(seconds);
        assertThat(service.getSecondsAmount(), is(seconds * 2));
    }

    @Test
    public void pace() {
        int paces = 1234;
        service.pace(paces);
        service.pace(paces);
        service.pace(paces);
        assertThat(service.getPacesAmount(), is(paces * 3));
    }

    @Test
    public void pacesLeft() {
        int paces = 1234;
        service.pace(paces);
        assertThat(service.getPacesLeft(), is(PACES_PER_DAY - paces));
    }

    @Test
    public void drinkLeft() {
        double drink = 220.32;
        service.drink(LocalDate.parse("2016-09-28"), drink);
        assertThat(service.getDrinkLeft(LocalDate.parse("2016-09-28")), closeTo(DRINK_PER_DAY - drink, EPSILON));
    }

    @Test
    public void moveLeft() {
        int seconds = 3600;
        service.move(seconds);
        assertThat(service.getMoveLeft(), is(MOVE_SECONDS_PER_DAY - seconds));
    }

    @Test
    public void eatLeft() {
        double calories = 254.3;
        service.eat(calories);
        assertThat(service.getCaloriesLeft(), closeTo(CALORIES_PER_DAY - calories, EPSILON));
    }
}