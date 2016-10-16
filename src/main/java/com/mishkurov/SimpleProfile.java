package com.mishkurov;

import java.util.HashMap;
import java.util.Map;

public class SimpleProfile implements Profile {

    private Map<Activity, Double> activityGoals;

    public SimpleProfile() {
        activityGoals = new HashMap<>();
        for (Activity activity : Activity.values()) {
            activityGoals.put(activity, activity.getDefaultValue());
        }
    }

    @Override
    public double getActivityGoal(Activity activity) {
        Double goal = activityGoals.get(activity);
        goal = goal == null ? 0 : goal;
        return goal;
    }

    @Override
    public void setActivityGoal(Activity activity, double goal) {
        this.activityGoals.put(activity, goal);
    }
}
