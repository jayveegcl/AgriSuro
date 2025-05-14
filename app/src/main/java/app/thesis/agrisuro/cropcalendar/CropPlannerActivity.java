package app.thesis.agrisuro.cropcalendar;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.thesis.agrisuro.R;

public class CropPlannerActivity extends AppCompatActivity {

    private TextView textViewStartDate;
    private Button buttonSelectDate;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<TaskPhase> taskPhases;
    private Calendar selectedDate = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private TaskPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_planner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        // Initialize views - MAKE SURE THESE IDS MATCH YOUR XML
        textViewStartDate = findViewById(R.id.textViewStartDate);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);

        // This is the critical part - ensure this ID exists in your layout
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        // Double check if recyclerView was found
        if (recyclerViewTasks == null) {
            Toast.makeText(this, "Error: RecyclerView not found in layout!", Toast.LENGTH_LONG).show();
            return; // Prevent NPE by exiting early
        }

        // Initialize preferences manager
        preferencesManager = new TaskPreferencesManager(this);

        // Setup RecyclerView
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskPhases = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskPhases, preferencesManager);
        recyclerViewTasks.setAdapter(taskAdapter);

        // Setup date selection
        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Load saved date or use current date
        long savedDateMillis = preferencesManager.getStartDate(selectedDate.getTimeInMillis());
        selectedDate.setTimeInMillis(savedDateMillis);

        // Initialize with saved date
        updateStartDate(selectedDate.getTime());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save task status when the app is paused
        if (taskAdapter != null) {
            taskAdapter.saveTaskStatus();
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Check if date is different from current
                        boolean dateChanged = selectedDate.get(Calendar.YEAR) != year ||
                                selectedDate.get(Calendar.MONTH) != month ||
                                selectedDate.get(Calendar.DAY_OF_MONTH) != dayOfMonth;

                        if (dateChanged) {
                            // Clear saved task statuses when date changes
                            preferencesManager.clearAllData();

                            // Update to new date
                            selectedDate.set(Calendar.YEAR, year);
                            selectedDate.set(Calendar.MONTH, month);
                            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateStartDate(selectedDate.getTime());
                        }
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateStartDate(Date startDate) {
        textViewStartDate.setText("Planting Date: " + dateFormat.format(startDate));
        // Save the selected date
        preferencesManager.saveStartDate(startDate.getTime());
        generateTaskPhases(startDate);
    }

    private void generateTaskPhases(Date startDate) {
        taskPhases.clear();

        // Calculate start date for preparation phase (17 days before the selected date)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, -17);

        // Add Pag-aararo phase (7 days before preparation)
        List<String> pagAararoTasks = new ArrayList<>();
        pagAararoTasks.add("Pag aararo");
        TaskPhase pagAararoPhase = new TaskPhase(
                "Land Preparation Phase",
                formatDateRange(calendar.getTime(), 7),
                pagAararoTasks
        );
        taskPhases.add(pagAararoPhase);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Add Preparation Phase
        List<String> prepTasks = new ArrayList<>();
        prepTasks.add("Pag aayos ng kanal at pilapil");
        prepTasks.add("Pagbababad ng lupa");
        prepTasks.add("Paghahanda ng lupang pupunlaan");
        TaskPhase prepPhase = new TaskPhase(
                "Preparation Phase",
                formatDateRange(calendar.getTime(), 7),
                prepTasks
        );
        taskPhases.add(prepPhase);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Vegetative Phase - Week 1
        List<String> vegWeek1Tasks = new ArrayList<>();
        vegWeek1Tasks.add("Pagbababad ng binhi");
        vegWeek1Tasks.add("Paggawa ng kamang-punlaan");
        vegWeek1Tasks.add("Pagkukulob ng binhi");
        vegWeek1Tasks.add("Pagpupunla");
        TaskPhase vegPhase1 = new TaskPhase(
                "Vegetative Phase - Week 1",
                formatDateRange(calendar.getTime(), 7),
                vegWeek1Tasks
        );
        taskPhases.add(vegPhase1);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Vegetative Phase - Week 2
        List<String> vegWeek2Tasks = new ArrayList<>();
        vegWeek2Tasks.add("Ika-unang pagsusuyod");
        vegWeek2Tasks.add("Pagpapatubig sa punlaan");
        vegWeek2Tasks.add("Pagpapataba sa punlaan");
        TaskPhase vegPhase2 = new TaskPhase(
                "Vegetative Phase - Week 2",
                formatDateRange(calendar.getTime(), 7),
                vegWeek2Tasks
        );
        taskPhases.add(vegPhase2);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Vegetative Phase - Week 3
        List<String> vegWeek3Tasks = new ArrayList<>();
        vegWeek3Tasks.add("Ika-pangalawa na pagsusuyod");
        vegWeek3Tasks.add("Paglilinang/pagpapatag");
        vegWeek3Tasks.add("Pagbubunot ng punla");
        vegWeek3Tasks.add("Pagkakalat ng punla");
        TaskPhase vegPhase3 = new TaskPhase(
                "Vegetative Phase - Week 3",
                formatDateRange(calendar.getTime(), 7),
                vegWeek3Tasks
        );
        taskPhases.add(vegPhase3);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Vegetative Phase - Week 4
        List<String> vegWeek4Tasks = new ArrayList<>();
        vegWeek4Tasks.add("Paglilipat-tanim");
        vegWeek4Tasks.add("Pamamahala sa damo");
        vegWeek4Tasks.add("Pamamahala sa kuhol");
        vegWeek4Tasks.add("Pamamahala sa daga");
        vegWeek4Tasks.add("Paghuhulip");
        vegWeek4Tasks.add("Pagpapatubig");
        TaskPhase vegPhase4 = new TaskPhase(
                "Vegetative Phase - Week 4",
                formatDateRange(calendar.getTime(), 7),
                vegWeek4Tasks
        );
        taskPhases.add(vegPhase4);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Vegetative Phase - Week 5
        List<String> vegWeek5Tasks = new ArrayList<>();
        vegWeek5Tasks.add("Ika-unang pagpapataba");
        vegWeek5Tasks.add("Pagpapatubig");
        TaskPhase vegPhase5 = new TaskPhase(
                "Vegetative Phase - Week 5",
                formatDateRange(calendar.getTime(), 7),
                vegWeek5Tasks
        );
        taskPhases.add(vegPhase5);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Vegetative Phase - Week 6
        List<String> vegWeek6Tasks = new ArrayList<>();
        vegWeek6Tasks.add("Pagpapatubig");
        vegWeek6Tasks.add("Pamamahala sa damo");
        TaskPhase vegPhase6 = new TaskPhase(
                "Vegetative Phase - Week 6",
                formatDateRange(calendar.getTime(), 7),
                vegWeek6Tasks
        );
        taskPhases.add(vegPhase6);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Vegetative Phase - Week 7
        List<String> vegWeek7Tasks = new ArrayList<>();
        vegWeek7Tasks.add("Paglalagay ng observation well (AWD)");
        vegWeek7Tasks.add("Ika-pangalawang pagpapataba");
        vegWeek7Tasks.add("Pagpapatubig");
        TaskPhase vegPhase7 = new TaskPhase(
                "Vegetative Phase - Week 7",
                formatDateRange(calendar.getTime(), 7),
                vegWeek7Tasks
        );
        taskPhases.add(vegPhase7);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Reproductive Phase - Week 1
        List<String> repWeek1Tasks = new ArrayList<>();
        repWeek1Tasks.add("Pagpapatubig");
        TaskPhase repPhase1 = new TaskPhase(
                "Reproductive Phase - Week 1",
                formatDateRange(calendar.getTime(), 7),
                repWeek1Tasks
        );
        taskPhases.add(repPhase1);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Reproductive Phase - Week 2
        List<String> repWeek2Tasks = new ArrayList<>();
        repWeek2Tasks.add("Pagpapatubig");
        repWeek2Tasks.add("Ika-pangatlong pagpapataba");
        TaskPhase repPhase2 = new TaskPhase(
                "Reproductive Phase - Week 2",
                formatDateRange(calendar.getTime(), 7),
                repWeek2Tasks
        );
        taskPhases.add(repPhase2);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Reproductive Phase - Weeks 3-6
        List<String> repWeek3Tasks = new ArrayList<>();
        repWeek3Tasks.add("Pagpapatubig");
        TaskPhase repPhase3 = new TaskPhase(
                "Reproductive Phase - Weeks 3-6",
                formatDateRange(calendar.getTime(), 25),
                repWeek3Tasks
        );
        taskPhases.add(repPhase3);
        calendar.add(Calendar.DAY_OF_MONTH, 25);

        // Ripening Phase - Week 1
        List<String> ripWeek1Tasks = new ArrayList<>();
        ripWeek1Tasks.add("Pagpapatubig");
        TaskPhase ripPhase1 = new TaskPhase(
                "Ripening Phase - Week 1",
                formatDateRange(calendar.getTime(), 10),
                ripWeek1Tasks
        );
        taskPhases.add(ripPhase1);
        calendar.add(Calendar.DAY_OF_MONTH, 10);

        // Ripening Phase - Week 2
        List<String> ripWeek2Tasks = new ArrayList<>();
        ripWeek2Tasks.add("Pagpapatubig");
        ripWeek2Tasks.add("Pagpapatuyo ng palayan");
        TaskPhase ripPhase2 = new TaskPhase(
                "Ripening Phase - Week 2",
                formatDateRange(calendar.getTime(), 7),
                ripWeek2Tasks
        );
        taskPhases.add(ripPhase2);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Wait 8 days for harvesting
        calendar.add(Calendar.DAY_OF_MONTH, 8);

        // Harvesting Phase
        List<String> harvestTasks = new ArrayList<>();
        harvestTasks.add("Pag-aani");
        TaskPhase harvestPhase = new TaskPhase(
                "Harvesting Phase",
                formatDateRange(calendar.getTime(), 7),
                harvestTasks
        );
        taskPhases.add(harvestPhase);

        // Load saved task states
        preferencesManager.loadTaskStatus(taskPhases);

        // Notify adapter of changes
        taskAdapter.notifyDataSetChanged();

        // Show confirmation to user
        Toast.makeText(this, "Task calendar updated", Toast.LENGTH_SHORT).show();
    }

    private String formatDateRange(Date startDate, int durationDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        String start = sdf.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, durationDays - 1);
        String end = sdf.format(calendar.getTime());

        return start + " - " + end + " (" + durationDays + " days)";
    }
}