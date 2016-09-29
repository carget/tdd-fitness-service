package com.mishkurov;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
        service.eat(LocalDate.parse("2016-09-27"), calories);
        service.eat(LocalDate.parse("2016-09-25"), calories);
        service.eat(LocalDate.parse("2016-09-25"), calories);
        service.eat(LocalDate.parse("2016-09-27"), calories);
        assertThat(service.getCaloriesAmount(LocalDate.parse("2016-09-25")), closeTo(calories * 2, EPSILON));
        assertThat(service.getCaloriesAmount(LocalDate.parse("2016-09-27")), closeTo(calories * 2, EPSILON));
    }

    @Test
    public void move() {
        int seconds = 3600;
        service.move(LocalDate.parse("2016-09-27"), seconds);
        service.move(LocalDate.parse("2016-09-27"), seconds);
        assertThat(service.getMoveSecondsAmount(LocalDate.parse("2016-09-27")), is(seconds * 2));
        assertThat(service.getMoveSecondsAmount(LocalDate.parse("2016-09-25")), is(seconds * 0));
    }

    @Test
    public void pace() {
        int paces = 1234;
        service.pace(LocalDate.parse("2016-09-27"), paces);
        service.pace(LocalDate.parse("2016-09-27"), paces);
        service.pace(LocalDate.parse("2016-09-27"), paces);
        assertThat(service.getPacesAmount(LocalDate.parse("2016-09-27")), is(paces * 3));
        assertThat(service.getPacesAmount(LocalDate.parse("2016-09-26")), is(paces * 0));
    }

    @Test
    public void pacesLeft() {
        int paces = 1234;
        service.pace(LocalDate.parse("2016-09-27"), paces);
        assertThat(service.getPacesLeft(LocalDate.parse("2016-09-27")), is(PACES_PER_DAY - paces));
        assertThat(service.getPacesLeft(LocalDate.parse("2016-09-26")), is(PACES_PER_DAY));
    }

    @Test
    public void drinkLeft() {
        double drink = 220.32;
        service.drink(LocalDate.parse("2016-09-28"), drink);
        service.drink(LocalDate.parse("2016-09-28"), drink);
        assertThat(service.getDrinkLeft(LocalDate.parse("2016-09-28")), closeTo(DRINK_PER_DAY - drink * 2, EPSILON));
        assertThat(service.getDrinkLeft(LocalDate.parse("2016-09-27")), closeTo(DRINK_PER_DAY - drink * 0, EPSILON));
    }

    @Test
    public void moveLeft() {
        int seconds = 3600;
        service.move(LocalDate.parse("2016-09-27"), seconds);
        assertThat(service.getMoveSecondsLeft(LocalDate.parse("2016-09-27")), is(MOVE_SECONDS_PER_DAY - seconds));
    }

    @Test
    public void eatLeft() {
        double calories = 254.3;
        service.eat(LocalDate.parse("2016-09-27"), calories);
        service.eat(LocalDate.parse("2016-09-27"), calories);
        service.eat(LocalDate.parse("2016-09-26"), calories);
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-27")), closeTo(CALORIES_PER_DAY - calories * 2, EPSILON));
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-26")), closeTo(CALORIES_PER_DAY - calories * 1, EPSILON));
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-25")), closeTo(CALORIES_PER_DAY - calories * 0, EPSILON));
    }

    @Test
    @Ignore
    public void reportPerDay() {
        LocalDate reportDate = LocalDate.parse("2016-09-27");
        FitnessService.Report serviceReport = service.getReport(reportDate);
        FitnessService.Report idealReport = new FitnessService.Report(reportDate, reportDate) {{
                setActivityPercent(Activity.EAT, 10.);
                setActivityPercent(Activity.DRINK, 10.);
                setActivityPercent(Activity.PACE, 10.);
                setActivityPercent(Activity.MOVE, 10.);
                setMedian(Activity.EAT, 10.);
                setMedian(Activity.DRINK, 10.);
                setMedian(Activity.PACE, 10.);
                setMedian(Activity.MOVE, 10.);
            }};
        assertThat(idealReport, is(idealReport));
    }

}