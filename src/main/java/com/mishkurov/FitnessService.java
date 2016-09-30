package com.mishkurov;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FitnessService {
    public static final double PACES_PER_DAY = 2000;
    public static final double DRINK_PER_DAY = 1500.;
    public static final double MOVE_SECONDS_PER_DAY = 7200;
    public static final double CALORIES_PER_DAY = 2300.;
    private Map<LocalDate, Map<Activity, Double>> allData;

    public FitnessService() {
        allData = new HashMap<>();
    }

    public void addAmount(LocalDate date, Activity activity, Double amount) {
        Map<Activity, Double> activityData = allData.get(date);
        if (activityData == null) {
            activityData = new HashMap<>();
            allData.put(date, activityData);
        }
        Double storedAmount = activityData.get(activity);
        storedAmount = storedAmount == null ? 0 : storedAmount;
        activityData.put(activity, storedAmount + amount);
    }

    public double getAmount(LocalDate date, Activity activity) {
        Map<Activity, Double> activityData = allData.get(date);
        if (activityData == null) {
            activityData = new HashMap<>();
            allData.put(date, activityData);
        }
        Double storedAmount = activityData.get(activity);
        return storedAmount == null ? 0 : storedAmount;
    }

    @Deprecated
    public void drink(LocalDate date, Double amount) {
        addAmount(date, Activity.DRINK, amount);
    }

    public double getDrinkAmount(LocalDate date) {
        return getAmount(date, Activity.DRINK);
    }

    @Deprecated
    public void eat(LocalDate date, Double calories) {
        addAmount(date, Activity.EAT, calories);
    }

    public Double getEatAmount(LocalDate date) {
        return getAmount(date, Activity.EAT);
    }

    @Deprecated
    public void move(LocalDate date, double seconds) {
        addAmount(date, Activity.MOVE,  seconds);
    }

    public double getMoveSecondsAmount(LocalDate date) {
        return getAmount(date, Activity.MOVE);
    }

    @Deprecated
    public void pace(LocalDate date, double paces) {
        addAmount(date, Activity.PACE, paces);
    }

    public double getPacesAmount(LocalDate date) {
        return getAmount(date, Activity.PACE);
    }

    public double getPacesLeft(LocalDate date) {
        double pacesRecorded = getPacesAmount(date);
        return PACES_PER_DAY - pacesRecorded;
    }

    public double getDrinkLeft(LocalDate date) {
        double consumedDrink = getDrinkAmount(date);
        return DRINK_PER_DAY - consumedDrink;
    }

    public double getMoveSecondsLeft(LocalDate date) {
        double moveSecondsRecorded = getMoveSecondsAmount(date);
        return MOVE_SECONDS_PER_DAY - moveSecondsRecorded;
    }

    public double getCaloriesLeft(LocalDate date) {
        double consumedCalories = getEatAmount(date);
        return CALORIES_PER_DAY - consumedCalories;
    }

    public Report getReport(LocalDate reportDate) {
        StringBuilder report = new StringBuilder("Report\n");
//        for () {
//        }
//
        return null;
    }

    public static class Report {
        private double eatPercent;
        private double drinkPercent;
        private double movePercent;
        private double pacePercent;
        private double eatMedian;
        private double drinkMedian;
        private double moveMedian;
        private double paceMedian;
        private LocalDate startDate;
        private LocalDate endDate;

        public Report(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.eatPercent = 0;
            this.drinkPercent = 0;
            this.movePercent = 0;
            this.pacePercent = 0;
            this.eatMedian = 0;
            this.drinkMedian = 0;
            this.moveMedian = 0;
            this.paceMedian = 0;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setActivityPercent(Activity activity, double percent) {
            switch (activity) {
                case EAT:
                    this.eatPercent = percent;
                    break;
                case DRINK:
                    this.drinkPercent = percent;
                    break;
                case MOVE:
                    this.movePercent = percent;
                    break;
                case PACE:
                    this.pacePercent = percent;
                    break;
            }
        }

        public double getActivityPercent(Activity activity) {
            switch (activity) {
                case EAT:
                    return this.eatPercent;
                case DRINK:
                    return this.drinkPercent;
                case MOVE:
                    return this.movePercent;
                case PACE:
                    return this.pacePercent;
                default:
                    return 0;
            }
        }

        public void setMedian(Activity activity, double median) {
            switch (activity) {
                case EAT:
                    this.eatMedian = median;
                    break;
                case DRINK:
                    this.drinkMedian = median;
                    break;
                case MOVE:
                    this.moveMedian = median;
                    break;
                case PACE:
                    this.paceMedian = median;
                    break;
            }
        }

        public double getMedian(Activity activity) {
            switch (activity) {
                case EAT:
                    return this.eatMedian;
                case DRINK:
                    return this.drinkMedian;
                case MOVE:
                    return this.moveMedian;
                case PACE:
                    return this.paceMedian;
                default:
                    return 0;
            }
        }

        @Override
        public boolean equals(Object otherReport) {
            if (otherReport instanceof Report) {
                //todo replace with double comparison
                return (((Report) otherReport).eatPercent == this.eatPercent &&
                        ((Report) otherReport).drinkPercent == this.drinkPercent &&
                        ((Report) otherReport).movePercent == this.movePercent &&
                        ((Report) otherReport).pacePercent == this.pacePercent &&
                        ((Report) otherReport).eatMedian == this.eatMedian &&
                        ((Report) otherReport).drinkMedian == this.drinkMedian &&
                        ((Report) otherReport).moveMedian == this.moveMedian &&
                        ((Report) otherReport).paceMedian == this.paceMedian);
            } else {
                return false;
            }
        }
    }
}

