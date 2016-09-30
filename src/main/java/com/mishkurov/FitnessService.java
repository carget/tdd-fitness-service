package com.mishkurov;

import java.time.LocalDate;
import java.util.*;

public class FitnessService {
    public static final double PACES_PER_DAY = 2000;
    public static final double DRINK_PER_DAY = 1500.;
    public static final double MOVE_SECONDS_PER_DAY = 7200;
    public static final double CALORIES_PER_DAY = 2300.;
    public static final double EPSILON = 1E-6;
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

    public double getDrinkAmount(LocalDate date) {
        return getAmount(date, Activity.DRINK);
    }

    public Double getEatAmount(LocalDate date) {
        return getAmount(date, Activity.EAT);
    }

    public double getMoveSecondsAmount(LocalDate date) {
        return getAmount(date, Activity.MOVE);
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

    public double getPercentForActivity(LocalDate date, Activity activity) {
        switch (activity) {
            case EAT:
                return (allData.get(date).get(activity) / CALORIES_PER_DAY) * 100.;
            case DRINK:
                return (allData.get(date).get(activity) / DRINK_PER_DAY) * 100.;
            case MOVE:
                return (allData.get(date).get(activity) / MOVE_SECONDS_PER_DAY) * 100.;
            case PACE:
                return (allData.get(date).get(activity) / PACES_PER_DAY) * 100.;
            default:
                return 0;
        }
    }

    public Report getReport(LocalDate startDate, LocalDate endDate) {
        return new Report(startDate, endDate);
    }

    public class Report {
        private double eatPercent;
        private double drinkPercent;
        private double movePercent;
        private double pacePercent;
        private double eatMedian;
        private double drinkMedian;
        private double moveMedian;
        private double paceMedian;
        private Map<Activity, Double> activityPercentMap;
        private Map<Activity, Double> activityMedianMap;
        private LocalDate startDate;
        private LocalDate endDate;

        public Report(LocalDate startDate, LocalDate endDate) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date!");
            }
            this.startDate = startDate;
            this.endDate = endDate;
            activityPercentMap = new HashMap<>();
            activityMedianMap = new HashMap<>();
            this.eatPercent = 0;
            this.drinkPercent = 0;
            this.movePercent = 0;
            this.pacePercent = 0;
            this.eatMedian = 0;
            this.drinkMedian = 0;
            this.moveMedian = 0;
            this.paceMedian = 0;
            calculate();
        }

        private void calculate() {
            LocalDate currDate = startDate;
            Map<Activity, ArrayList<Double>> percentsPerDay = new HashMap<>();
            for (Activity a : Activity.values()) {
                percentsPerDay.put(a, new ArrayList<>());
            }
            while (!currDate.isAfter(endDate)) {
                Map<Activity, Double> dataPerDay = allData.get(currDate);
                for (Activity a : Activity.values()) {
                    Double valueForActivity = dataPerDay.get(a);
                    valueForActivity = valueForActivity == null ? 0 : valueForActivity;
                    percentsPerDay.get(a).add(valueForActivity);
                }
                currDate = currDate.plusDays(1);
            }
            for (Activity a : Activity.values()) {
                ArrayList<Double> activityValues = percentsPerDay.get(a);
                Collections.sort(activityValues);
                Double median = 0.;
                int numberOfValues = activityValues.size();
                if (numberOfValues % 2 == 0) {
                    median = (activityValues.get(numberOfValues / 2) + activityValues.get(numberOfValues / 2 + 1)) / 2;
                } else {
                    median = activityValues.get(numberOfValues / 2);
                }
                activityMedianMap.put(a, median);

//                activityPercentMap.put(a, activityValues)
            }
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
                return (Math.abs(Double.compare(((Report) otherReport).eatPercent, this.eatPercent)) < EPSILON &&
                        Math.abs(Double.compare(((Report) otherReport).drinkPercent, this.drinkPercent)) < EPSILON &&
                        Math.abs(Double.compare(((Report) otherReport).movePercent, this.movePercent)) < EPSILON &&
                        Math.abs(Double.compare(((Report) otherReport).pacePercent, this.pacePercent)) < EPSILON &&
                        Math.abs(Double.compare(((Report) otherReport).eatMedian, this.eatMedian)) < EPSILON &&
                        Math.abs(Double.compare(((Report) otherReport).drinkMedian, this.drinkMedian)) < EPSILON &&
                        Math.abs(Double.compare(((Report) otherReport).moveMedian, this.moveMedian)) < EPSILON &&
                        Math.abs(Double.compare(((Report) otherReport).paceMedian, this.paceMedian)) < EPSILON);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            StringBuilder report = new StringBuilder();
            report.append("Start date: " + this.startDate + " End date: " + this.endDate + "\n");
            for (Activity a : Activity.values()) {
                report.append("Percent for (" + a + ")=" + activityPercentMap.get(a) + ", ");
                report.append("Median for (" + a + ")=" + activityMedianMap.get(a) + ", ");
            }
            return report.toString();
        }
    }

}

