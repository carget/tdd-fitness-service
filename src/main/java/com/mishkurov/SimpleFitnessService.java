package com.mishkurov;

import java.time.LocalDate;
import java.util.*;

public class SimpleFitnessService implements FitnessService {

    private Profile profile;
    private Map<LocalDate, Map<Activity, Double>> allData;

    public SimpleFitnessService() {
        allData = new HashMap<>();
        profile = new SimpleProfile();
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
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

    @Override
    public double getAmount(LocalDate date, Activity activity) {
        Map<Activity, Double> activityData = allData.get(date);
        if (activityData == null) {
            activityData = new HashMap<>();
            allData.put(date, activityData);
        }
        Double storedAmount = activityData.get(activity);
        return storedAmount == null ? 0 : storedAmount;
    }

    @Override
    public double getAmountLeft(LocalDate date, Activity activity) {
        Map<Activity, Double> activityPerDay = allData.get(date);
        double activityGoal = profile.getActivityGoal(activity);
        if (activityPerDay == null) {
            return activityGoal;
        }
        Double amountLeft = activityPerDay.get(activity);
        amountLeft = amountLeft == null ? 0 : amountLeft;
        return activityGoal - amountLeft;
    }

    @Override
    public double getPercentForActivity(Activity activity, double value) {
        return value / profile.getActivityGoal(activity) * 100;
    }

    @Override
    public Report getReport(LocalDate startDate, LocalDate endDate) {
        return new Report(startDate, endDate);
    }

    class Report {
        private Map<Activity, Double> activityPercentMap;
        private Map<Activity, Double> activityMedianMap;
        private LocalDate startDate;
        private LocalDate endDate;

        Report(LocalDate startDate, LocalDate endDate) {
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

