package com.mishkurov;

import java.time.LocalDate;
import java.util.*;

public class FitnessService {

    public static final double EPSILON = 1E-6;

    private Profile profile;
    private Map<LocalDate, Map<Activity, Double>> allData;

    public FitnessService() {
        allData = new HashMap<>();
        profile = new Profile();
    }

    public Profile getProfile() {
        return profile;
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

    public double getAmountLeft(LocalDate date, Activity activity) {
        Map<Activity, Double> activityPerDay = allData.get(date);
        if (activityPerDay == null) {
            return profile.getActivityGoal(activity);
        }
        Double amountLeft = activityPerDay.get(activity);
        amountLeft = amountLeft == null ? 0 : amountLeft;
        return profile.getActivityGoal(activity) - amountLeft;
    }

    @Deprecated
    public double getDrinkAmount(LocalDate date) {
        return getAmount(date, Activity.DRINK);
    }

    @Deprecated
    public double getEatAmount(LocalDate date) {
        return getAmount(date, Activity.EAT);
    }

    @Deprecated
    public double getMoveSecondsAmount(LocalDate date) {
        return getAmount(date, Activity.MOVE);
    }

    @Deprecated
    public double getPacesAmount(LocalDate date) {
        return getAmount(date, Activity.PACE);
    }

    @Deprecated
    public double getPacesLeft(LocalDate date) {
        return profile.getActivityGoal(Activity.PACE) - getPacesAmount(date);
    }

    @Deprecated
    public double getDrinkLeft(LocalDate date) {
        return profile.getActivityGoal(Activity.DRINK) - getDrinkAmount(date);
    }

    @Deprecated
    public double getMoveSecondsLeft(LocalDate date) {
        return profile.getActivityGoal(Activity.MOVE) - getMoveSecondsAmount(date);
    }

    @Deprecated
    public double getCaloriesLeft(LocalDate date) {
        return profile.getActivityGoal(Activity.EAT) - getEatAmount(date);
    }

    public double getPercentForActivity(Activity activity, double value) {
        return value / profile.getActivityGoal(activity) * 100;
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

