package app.thesis.agrisuro.cropcalendar;

import java.util.ArrayList;
import java.util.List;

public class TaskPhase {
    private String phaseName;
    private String dateRange;
    private List<String> tasks;
    private List<Boolean> taskCompletionStatus;

    public TaskPhase(String phaseName, String dateRange, List<String> tasks) {
        this.phaseName = phaseName;
        this.dateRange = dateRange;
        this.tasks = tasks;
        // Initialize all tasks as incomplete
        this.taskCompletionStatus = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            taskCompletionStatus.add(false);
        }
    }

    public String getPhaseName() {
        return phaseName;
    }

    public String getDateRange() {
        return dateRange;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public List<Boolean> getTaskCompletionStatus() {
        return taskCompletionStatus;
    }

    public void setTaskCompleted(int position, boolean completed) {
        if (position >= 0 && position < taskCompletionStatus.size()) {
            taskCompletionStatus.set(position, completed);
        }
    }

    // Unique identifier for saving/loading task states
    public String getUniqueId() {
        return phaseName + "_" + dateRange.replace(" ", "_").replace("(", "").replace(")", "");
    }
}