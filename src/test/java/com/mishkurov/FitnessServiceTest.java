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
        assertThat(service.getPacesLeft(LocalDate.parse("2016-09-27")),
                is(service.getProfile().getActivityGoal(Activity.PACE) - paces));
        assertThat(service.getPacesLeft(LocalDate.parse("2016-09-26")),
                is(service.getProfile().getActivityGoal(Activity.PACE)));
    }

    @Test
    public void pacesLeftProfile() {
        double paces = 1234;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.PACE, paces);
        assertThat(service.getAmountLeft(LocalDate.parse("2016-09-27"), Activity.PACE),
                is(service.getProfile().getActivityGoal(Activity.PACE) - paces));
        assertThat(service.getPacesLeft(LocalDate.parse("2016-09-26")),
                is(service.getProfile().getActivityGoal(Activity.PACE)));
    }

    @Test
    public void drinkLeft() {
        double drink = 220.32;
        service.addAmount(LocalDate.parse("2016-09-28"), Activity.DRINK, drink);
        service.addAmount(LocalDate.parse("2016-09-28"), Activity.DRINK, drink);
        assertThat(service.getDrinkLeft(LocalDate.parse("2016-09-28")),
                closeTo(service.getProfile().getActivityGoal(Activity.DRINK) - drink * 2, EPSILON));
        assertThat(service.getDrinkLeft(LocalDate.parse("2016-09-27")),
                closeTo(service.getProfile().getActivityGoal(Activity.DRINK) - drink * 0, EPSILON));
    }

    @Test
    public void moveLeft() {
        double seconds = 3600;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.MOVE, seconds);
        assertThat(service.getMoveSecondsLeft(LocalDate.parse("2016-09-27")),
                is(service.getProfile().getActivityGoal(Activity.MOVE) - seconds));
    }

    @Test
    public void eatLeft() {
        double calories = 254.3;
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.EAT, calories);
        service.addAmount(LocalDate.parse("2016-09-27"), Activity.EAT, calories);
        service.addAmount(LocalDate.parse("2016-09-26"), Activity.EAT, calories);
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-27")),
                closeTo(service.getProfile().getActivityGoal(Activity.EAT) - calories * 2, EPSILON));
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-26")),
                closeTo(service.getProfile().getActivityGoal(Activity.EAT) - calories * 1, EPSILON));
        assertThat(service.getCaloriesLeft(LocalDate.parse("2016-09-25")),
                closeTo(service.getProfile().getActivityGoal(Activity.EAT) - calories * 0, EPSILON));
    }

    @Test
    public void reportPerDay() {
        LocalDate reportDate = LocalDate.parse("2016-09-27");
        service.addAmount(reportDate, Activity.EAT, 10.);
        service.addAmount(reportDate, Activity.DRINK, 100.);
        service.addAmount(reportDate, Activity.MOVE, 1000.);
        service.addAmount(reportDate, Activity.PACE, 10000.);
        FitnessService.Report serviceReport = service.getReport(reportDate, reportDate);
        FitnessService.Report idealReport = service.new Report(reportDate, reportDate);
        idealReport.setActivityPercent(Activity.EAT, 1);
        idealReport.setActivityPercent(Activity.DRINK, 10);
        idealReport.setActivityPercent(Activity.MOVE, 100);
        idealReport.setActivityPercent(Activity.PACE, 1000);
        idealReport.setMedian(Activity.EAT, 1);
        idealReport.setMedian(Activity.DRINK, 10);
        idealReport.setMedian(Activity.MOVE, 100);
        idealReport.setMedian(Activity.PACE, 1000);
        assertThat(serviceReport, is(idealReport));
    }

    @Test
    public void reportPer3Day() {
        LocalDate startDate = LocalDate.parse("2016-09-27");
        LocalDate currDate = startDate.plusDays(0);
        service.addAmount(currDate, Activity.EAT, 1.);
        service.addAmount(currDate, Activity.DRINK, 10.);
        service.addAmount(currDate, Activity.PACE, 100.);
        service.addAmount(currDate, Activity.MOVE, 13.);
        currDate = LocalDate.parse("2016-09-28");
        service.addAmount(currDate, Activity.EAT, 2.);
        service.addAmount(currDate, Activity.DRINK, 10.);
        service.addAmount(currDate, Activity.PACE, 200.);
        service.addAmount(currDate, Activity.MOVE, 5.);
        currDate = LocalDate.parse("2016-09-29");
        service.addAmount(currDate, Activity.EAT, 3.);
        service.addAmount(currDate, Activity.DRINK, 20.);
        service.addAmount(currDate, Activity.PACE, 100.);
        service.addAmount(currDate, Activity.MOVE, 20.);
        currDate = LocalDate.parse("2016-09-30");
        service.addAmount(currDate, Activity.EAT, 4.);
        service.addAmount(currDate, Activity.DRINK, 20.);
        service.addAmount(currDate, Activity.PACE, 100.);
        service.addAmount(currDate, Activity.MOVE, 20.);
        LocalDate endDate = LocalDate.parse("2016-09-30");
        FitnessService.Report serviceReport = service.getReport(startDate, endDate);
        FitnessService.Report idealReport = service.new Report(startDate, endDate);
        idealReport.setActivityPercent(Activity.EAT, .25);
        idealReport.setActivityPercent(Activity.DRINK, 1.5);
        idealReport.setActivityPercent(Activity.MOVE, 1.45);
        idealReport.setActivityPercent(Activity.PACE, 12.5);
        idealReport.setMedian(Activity.EAT, .25);
        idealReport.setMedian(Activity.DRINK, 1.5);
        idealReport.setMedian(Activity.PACE, 10.);
        idealReport.setMedian(Activity.MOVE, 1.65);
        assertThat(serviceReport, is(idealReport));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testStartEndDatesValues() {
        LocalDate startDate = LocalDate.parse("2016-09-27");
        LocalDate endDate = LocalDate.parse("2016-09-26");
        FitnessService.Report report = service.getReport(startDate, endDate);
    }

}