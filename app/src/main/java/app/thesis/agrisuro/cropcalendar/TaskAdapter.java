package app.thesis.agrisuro.cropcalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.thesis.agrisuro.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskPhase> taskPhases;
    private TaskPreferencesManager preferencesManager;

    public TaskAdapter(List<TaskPhase> taskPhases, TaskPreferencesManager preferencesManager) {
        this.taskPhases = taskPhases;
        this.preferencesManager = preferencesManager;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task_phase, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskPhase taskPhase = taskPhases.get(position);
        holder.bind(taskPhase, position);
    }

    @Override
    public int getItemCount() {
        return taskPhases.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPhaseName;
        private TextView textViewDateRange;
        private ViewGroup taskContainer;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPhaseName = itemView.findViewById(R.id.textViewPhaseName);
            textViewDateRange = itemView.findViewById(R.id.textViewDateRange);
            taskContainer = itemView.findViewById(R.id.taskContainer);
        }

        public void bind(final TaskPhase taskPhase, final int phasePosition) {
            textViewPhaseName.setText(taskPhase.getPhaseName());
            textViewDateRange.setText(taskPhase.getDateRange());

            // Clear previous tasks
            taskContainer.removeAllViews();

            // Add task checkboxes with their saved state
            List<String> tasks = taskPhase.getTasks();
            List<Boolean> completionStatus = taskPhase.getTaskCompletionStatus();

            for (int i = 0; i < tasks.size(); i++) {
                final int taskPosition = i;
                CheckBox checkBox = new CheckBox(itemView.getContext());
                checkBox.setText(tasks.get(i));

                // Check if the completion status is available
                if (completionStatus != null && taskPosition < completionStatus.size()) {
                    checkBox.setChecked(completionStatus.get(taskPosition));
                }

                // Set listener to update the task completion status
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        taskPhase.setTaskCompleted(taskPosition, isChecked);
                        // Save the updated status
                        preferencesManager.saveTaskStatus(taskPhases);
                    }
                });

                taskContainer.addView(checkBox);
            }
        }
    }

    public void saveTaskStatus() {
        if (preferencesManager != null) {
            preferencesManager.saveTaskStatus(taskPhases);
        }
    }
}