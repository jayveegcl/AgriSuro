package app.thesis.agrisuro.cropcalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.thesis.agrisuro.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskPhase> taskPhases;

    public TaskAdapter(List<TaskPhase> taskPhases) {
        this.taskPhases = taskPhases;
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
        holder.bind(taskPhase);
    }

    @Override
    public int getItemCount() {
        return taskPhases.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPhaseName;
        private TextView textViewDateRange;
        private ViewGroup taskContainer;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPhaseName = itemView.findViewById(R.id.textViewPhaseName);
            textViewDateRange = itemView.findViewById(R.id.textViewDateRange);
            taskContainer = itemView.findViewById(R.id.taskContainer);
        }

        public void bind(TaskPhase taskPhase) {
            textViewPhaseName.setText(taskPhase.getPhaseName());
            textViewDateRange.setText(taskPhase.getDateRange());

            // Clear previous tasks
            taskContainer.removeAllViews();

            // Add task checkboxes
            for (String task : taskPhase.getTasks()) {
                CheckBox checkBox = new CheckBox(itemView.getContext());
                checkBox.setText(task);
                taskContainer.addView(checkBox);
            }
        }
    }
}
