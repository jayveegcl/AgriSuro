package app.thesis.agrisuro.cropcalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

public class TaskPreferencesManager {
    private static final String PREF_NAME = "AgrisuroTaskPreferences";
    private static final String TAG = "TaskPreferencesManager";
    private SharedPreferences sharedPreferences;

    public TaskPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save task completion status
    public void saveTaskStatus(List<TaskPhase> taskPhases) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            // Save each task's status
            for (TaskPhase phase : taskPhases) {
                String phaseId = phase.getUniqueId();
                List<Boolean> statuses = phase.getTaskCompletionStatus();

                for (int i = 0; i < statuses.size(); i++) {
                    String key = phaseId + "_task_" + i;
                    editor.putBoolean(key, statuses.get(i));
                }
            }

            editor.apply();
            Log.d(TAG, "Task statuses saved successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error saving task statuses: " + e.getMessage(), e);
        }
    }

    // Load task completion status
    public void loadTaskStatus(List<TaskPhase> taskPhases) {
        try {
            for (TaskPhase phase : taskPhases) {
                String phaseId = phase.getUniqueId();
                List<Boolean> statuses = phase.getTaskCompletionStatus();

                for (int i = 0; i < statuses.size(); i++) {
                    String key = phaseId + "_task_" + i;
                    boolean isCompleted = sharedPreferences.getBoolean(key, false);
                    phase.setTaskCompleted(i, isCompleted);
                }
            }
            Log.d(TAG, "Task statuses loaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error loading task statuses: " + e.getMessage(), e);
        }
    }

    // Save the selected start date
    public void saveStartDate(long dateInMillis) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("start_date", dateInMillis);
            editor.apply();
            Log.d(TAG, "Start date saved: " + dateInMillis);
        } catch (Exception e) {
            Log.e(TAG, "Error saving start date: " + e.getMessage(), e);
        }
    }

    // Get the saved start date
    public long getStartDate(long defaultValue) {
        try {
            long date = sharedPreferences.getLong("start_date", defaultValue);
            Log.d(TAG, "Retrieved start date: " + date);
            return date;
        } catch (Exception e) {
            Log.e(TAG, "Error getting start date: " + e.getMessage(), e);
            return defaultValue;
        }
    }

    // Clear all saved data (useful when the user changes the start date)
    public void clearAllData() {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Log.d(TAG, "All preferences data cleared");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing preferences data: " + e.getMessage(), e);
        }
    }
}