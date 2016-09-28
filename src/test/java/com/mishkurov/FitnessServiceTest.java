package com.mishkurov;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Anton_Mishkurov
 */
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
    public void drink() {
        Double amount = 100.;
        service.drink(amount);
        assertThat(service.getDrinkAmount(), is(100.));
    }

    @Test
    public void eat() {
        Double calories=15.;
        service.eat(calories);
        assertThat(service.getCaloriesAmount(), is(15.));
    }

}