package com.mishkurov;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    private Map<Activity, Double> activityGoals;

    public Profile() {
        activityGoals = new HashMap<>();
        for (Activity activity : Activity.values()) {
            activityGoals.put(activity, activity.getDefaultValue());
        }
    }

    public double getActivityGoal(Activity activity) {
        Double goal = activityGoals.get(activity);
        goal = goal == null ? 0 : goal;
        return goal;
    }

    public void setActivityGoal(Activity activity, double goal) {
        this.activityGoals.put(activity, goal);
    }
}
