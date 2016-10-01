package com.mishkurov;

import java.time.LocalDate;
import java.util.*;

public class FitnessService {
    public static final double CALORIES_PER_DAY = 1000;
    public static final double PACES_PER_DAY = 1000.;
    public static final double DRINK_PER_DAY = 1000;
    public static final double MOVE_SECONDS_PER_DAY = 1000;
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

    public double getEatAmount(LocalDate date) {
        return getAmount(date, Activity.EAT);
    }

    public double getMoveSecondsAmount(LocalDate date) {
        return getAmount(date, Activity.MOVE);
    }

    public double getPacesAmount(LocalDate date) {
        return getAmount(date, Activity.PACE);
    }

    public double getPacesLeft(LocalDate date) {
        return PACES_PER_DAY - getPacesAmount(date);
    }

    public double getDrinkLeft(LocalDate date) {
        return DRINK_PER_DAY - getDrinkAmount(date);
    }

    public double getMoveSecondsLeft(LocalDate date) {
        return MOVE_SECONDS_PER_DAY - getMoveSecondsAmount(date);
    }

    public double getCaloriesLeft(LocalDate date) {
        return CALORIES_PER_DAY - getEatAmount(date);
    }

    public double getPercentForActivity(Activity activity, double value) {
        switch (activity) {
            case EAT:
                return value / CALORIES_PER_DAY * 100.;
            case DRINK:
                return value / DRINK_PER_DAY * 100.;
            case MOVE:
                return value / MOVE_SECONDS_PER_DAY * 100.;
            case PACE:
                return value / PACES_PER_DAY * 100.;
            default:
                return 0;
        }
    }

    public Report getReport(LocalDate startDate, LocalDate endDate) {
        return new Report(startDate, endDate);
    }

    public class Report {
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
            calculate();
        }

        private void calculate() {
            LocalDate currDate = startDate;
            Map<Activity, ArrayList<Double>> percentsPerDay = new HashMap<>();
            for (Activity a : Activity.values()) {
                percentsPerDay.put(a, new ArrayList<>());
            }
            while (!currDate.isAfter(endDate)) {
                for (Activity a : Activity.values()) {
                    Double valueForActivity = allData.get(currDate).get(a);
                    valueForActivity = valueForActivity == null ? 0 : valueForActivity;
                    percentsPerDay.get(a).add(getPercentForActivity(a, valueForActivity));
                }
                currDate = currDate.plusDays(1);
            }
            for (Activity a : Activity.values()) {
                ArrayList<Double> activityValues = percentsPerDay.get(a);
                Collections.sort(activityValues);
                activityMedianMap.put(a, calcMedian(activityValues));
                activityPercentMap.put(a, calcAvg(activityValues));
            }
        }

        private double calcAvg(List<Double> list) {
            if (list.size() == 0) return 0;
            Double sum = 0.;
            for (Double v : list) {
                sum += v;
            }
            return sum / list.size();
        }

        private double calcMedian(List<Double> list) {
            if (list.size() == 0) return 0;
            if (list.size() == 1) return list.get(0);
            int listSize = list.size();
            if (listSize % 2 == 0) {
                return (list.get(listSize / 2 - 1) + list.get(listSize / 2)) / 2;
            } else {
                return list.get(listSize / 2);
            }
        }

        public void setActivityPercent(Activity activity, double percent) {
            activityPercentMap.put(activity, percent);
        }

        public double getActivityPercent(Activity activity) {
            return activityPercentMap.get(activity);
        }

        public void setMedian(Activity activity, double median) {
            activityMedianMap.put(activity, median);
        }

        public double getMedian(Activity activity) {
            return activityMedianMap.get(activity);
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        @Override
        public boolean equals(Object otherReport) {
            if (otherReport instanceof Report) {
                if (!(((Report) otherReport).getStartDate().isEqual(this.getStartDate()) &&
                        ((Report) otherReport).getEndDate().isEqual(this.getEndDate()))) {
                    return false;
                }
                for (Activity a : Activity.values()) {
                    if (!(Math.abs(Double.compare(((Report) otherReport).getActivityPercent(a),
                            this.getActivityPercent(a))) < EPSILON &&
                            Math.abs(Double.compare(((Report) otherReport).getMedian(a), this.getMedian(a))) < EPSILON)) {
                        return false;
                    }
                }
                return true;
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
                report.append("Median for (" + a + ")=" + activityMedianMap.get(a) + ", \n");
            }
            return report.toString();
        }
    }

}

