package app.thesis.agrisuro.cropcalendar;

import java.util.List;

public class TaskPhase {
    private String phaseName;
    private String dateRange;
    private List<String> tasks;

    public TaskPhase(String phaseName, String dateRange, List<String> tasks) {
        this.phaseName = phaseName;
        this.dateRange = dateRange;
        this.tasks = tasks;
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
}
