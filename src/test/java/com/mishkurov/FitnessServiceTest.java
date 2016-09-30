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

    private FitnessService service;

    @Before
    public void setUp() throws Exception {
        service = new FitnessService();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addAmount() {
        Double amount = 100.;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.DRINK, amount);
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.EAT, amount * 2);
        service.addAmount(LocalDate.parse("2016-09-26"), Activity.MOVE, amount * 3);
        service.addAmount(LocalDate.parse("2016-09-26"), Activity.MOVE, amount * 3);
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.DRINK), closeTo(amount * 1., EPSILON));
        assertThat(service.getAmount(LocalDate.parse("2016-09-26"), Activity.MOVE), closeTo(amount * 6., EPSILON));
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.MOVE), closeTo(amount * 0., EPSILON));
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.EAT), closeTo(amount * 2., EPSILON));
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.PACE), closeTo(amount * 0., EPSILON));
    }

    @Test
    public void drink() {
        Double amount = 100.;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.DRINK, amount);
        service.addAmount(LocalDate.parse("2016-09-28"), Activity.DRINK, amount);
        service.addAmount(LocalDate.parse("2016-09-28"), Activity.DRINK, amount);
        service.addAmount(LocalDate.parse("2016-09-29"), Activity.DRINK, amount);
        assertThat(service.getAmount(LocalDate.parse("2016-09-28"), Activity.DRINK), closeTo(amount * 2., EPSILON));
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.DRINK), closeTo(amount * 1., EPSILON));
        assertThat(service.getAmount(LocalDate.parse("2016-09-26"), Activity.DRINK), closeTo(amount * 0., EPSILON));
    }

    @Test
    public void eat() {
        Double calories = 15.;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.EAT, calories);
        service.addAmount(LocalDate.parse("2016-09-25"), Activity.EAT, calories);
        service.addAmount(LocalDate.parse("2016-09-25"), Activity.EAT, calories);
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.EAT, calories);
        assertThat(service.getAmount(LocalDate.parse("2016-09-25"), Activity.EAT), closeTo(calories * 2, EPSILON));
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.EAT), closeTo(calories * 2, EPSILON));
    }

    @Test
    public void move() {
        double seconds = 3600;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.MOVE, seconds);
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.MOVE, seconds);
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.MOVE), is(seconds * 2.));
        assertThat(service.getAmount(LocalDate.parse("2016-09-25"), Activity.MOVE), is(seconds * 0.));
    }

    @Test
    public void pace() {
        double paces = 1234;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.PACE, paces);
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.PACE, paces);
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.PACE, paces);
        assertThat(service.getAmount(LocalDate.parse("2016-09-27"), Activity.PACE), is(paces * 3));
        assertThat(service.getAmount(LocalDate.parse("2016-09-22"), Activity.PACE), is(paces * 0));
    }

    @Test
    public void pacesLeft() {
        double paces = 1234;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.PACE, paces);
        assertThat(service.getPacesLeft(LocalDate.parse("2016-09-27")), is(PACES_PER_DAY - paces));
        assertThat(service.getPacesLeft(LocalDate.parse("2016-09-26")), is(PACES_PER_DAY));
    }

    @Test
    public void drinkLeft() {
        double drink = 220.32;
        service.addAmount(LocalDate.parse("2016-09-28"), Activity.DRINK, drink);
        service.addAmount(LocalDate.parse("2016-09-28"), Activity.DRINK, drink);
        assertThat(service.getDrinkLeft(LocalDate.parse("2016-09-28")), closeTo(DRINK_PER_DAY - drink * 2, EPSILON));
        assertThat(service.getDrinkLeft(LocalDate.parse("2016-09-27")), closeTo(DRINK_PER_DAY - drink * 0, EPSILON));
    }

    @Test
    public void moveLeft() {
        double seconds = 3600;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.MOVE, seconds);
        assertThat(service.getMoveSecondsLeft(LocalDate.parse("2016-09-27")), is(MOVE_SECONDS_PER_DAY - seconds));
    }

    @Test
    public void eatLeft() {
        double calories = 254.3;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.EAT, calories);
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.EAT, calories);
        service.addAmount(LocalDate.parse("2016-09-26"), Activity.EAT, calories);
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-27")), closeTo(CALORIES_PER_DAY - calories * 2, EPSILON));
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-26")), closeTo(CALORIES_PER_DAY - calories * 1, EPSILON));
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-25")), closeTo(CALORIES_PER_DAY - calories * 0, EPSILON));
    }

    @Test
    public void reportPerDay() {
        LocalDate reportDate = LocalDate.parse("2016-09-27");
        for (int i = 0; i++ < 10; ) {
            service.addAmount(reportDate, Activity.EAT, i * 1.);  //45
            service.addAmount(reportDate, Activity.DRINK, i * 10.); //450
            service.addAmount(reportDate, Activity.PACE, i * 100.); //4500
            service.addAmount(reportDate, Activity.MOVE, 100.); //1000
        }
        FitnessService.Report serviceReport = service.getReport(reportDate, reportDate);
        FitnessService.Report idealReport =  service.new Report(reportDate, reportDate) {{
//            setActivityPercent(Activity.EAT, 45 / CALORIES_PER_DAY * 100);
//            setActivityPercent(Activity.DRINK, 450 / DRINK_PER_DAY * 100);
//            setActivityPercent(Activity.PACE, 4500 / PACES_PER_DAY * 100);
//            setActivityPercent(Activity.MOVE, 1000 / MOVE_SECONDS_PER_DAY * 100);
            setMedian(Activity.EAT, 4.5);
            setMedian(Activity.DRINK, 45);
            setMedian(Activity.PACE, 450.);
            setMedian(Activity.MOVE, 100.);
        }};
        System.out.println("service Report");
        System.out.println(serviceReport);
        System.out.println("ideal Report");
        System.out.println(idealReport);
        assertThat(serviceReport, is(idealReport));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartEndDatesValues() {
        LocalDate startDate = LocalDate.parse("2016-09-27");
        LocalDate endDate = LocalDate.parse("2016-09-26");
        FitnessService.Report report = service.getReport(startDate, endDate);
    }


}