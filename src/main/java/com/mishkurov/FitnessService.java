package com.mishkurov;

import java.time.LocalDate;

/**
 * //TODO add comments
 *
 * @author Anton Mishkyroff
 */
public interface FitnessService {
    double EPSILON = 1E-6;

    Profile getProfile();

    void addAmount(LocalDate date, Activity activity, Double amount);

    double getAmount(LocalDate date, Activity activity);

    double getAmountLeft(LocalDate date, Activity activity);

    double getPercentForActivity(Activity activity, double value);

    SimpleFitnessService.Report getReport(LocalDate startDate, LocalDate endDate);
}
