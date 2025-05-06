package app.thesis.agrisuro.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.thesis.agrisuro.R;

public class ExpenseTrackerFragment extends Fragment {

    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter adapter;
    private TextView totalExpensesTextView;
    private Spinner categoryFilterSpinner;
    private TextView selectedDateRangeTextView;
    private Button filterButton;
    private long startDate = 0;
    private long endDate = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_tracker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        RecyclerView recyclerView = view.findViewById(R.id.expenses_recycler_view);
        FloatingActionButton addExpenseFab = view.findViewById(R.id.add_expense_fab);
        totalExpensesTextView = view.findViewById(R.id.total_expenses_text_view);
        categoryFilterSpinner = view.findViewById(R.id.category_filter_spinner);
        selectedDateRangeTextView = view.findViewById(R.id.selected_date_range_text_view);
        filterButton = view.findViewById(R.id.filter_button);
        Button dateRangeButton = view.findViewById(R.id.date_range_button);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        // Set up item click listener for deletion
        adapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                showDeleteConfirmationDialog(expense);
            }
        });

        // Observe expenses from ViewModel
        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.setExpenses(expenses);
            updateTotalExpenses(expenses);
        });

        // Set up date range picker
        dateRangeButton.setOnClickListener(v -> showDateRangePicker());

        // Set up filter button
        filterButton.setOnClickListener(v -> applyFilters());

        // Set up add expense button
        addExpenseFab.setOnClickListener(v -> showAddExpenseDialog());
    }

    private void showDeleteConfirmationDialog(Expense expense) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Expense");
        builder.setMessage("Are you sure you want to delete this expense?\n\n" +
                expense.getDescription() + " - ₱" + String.format(Locale.getDefault(), "%.2f", expense.getAmount()));

        builder.setPositiveButton("Delete", (dialog, which) -> {
            expenseViewModel.delete(expense);
            Toast.makeText(requireContext(), "Expense deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showDateRangePicker() {
        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Date Range")
                .setSelection(androidx.core.util.Pair.create(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()))
                .build();

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            startDate = selection.first;
            endDate = selection.second;

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String startDateStr = sdf.format(new Date(startDate));
            String endDateStr = sdf.format(new Date(endDate));

            selectedDateRangeTextView.setText(String.format("From %s to %s", startDateStr, endDateStr));
        });

        dateRangePicker.show(getParentFragmentManager(), "DATE_RANGE_PICKER");
    }

    private void applyFilters() {
        String selectedCategory = categoryFilterSpinner.getSelectedItem().toString();

        if ("All Categories".equals(selectedCategory)) {
            if (startDate > 0 && endDate > 0) {
                expenseViewModel.getExpensesByDateRange(startDate, endDate).observe(
                        getViewLifecycleOwner(), expenses -> {
                            adapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            } else {
                expenseViewModel.getAllExpenses().observe(
                        getViewLifecycleOwner(), expenses -> {
                            adapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            }
        } else {
            if (startDate > 0 && endDate > 0) {
                expenseViewModel.getExpensesByCategoryAndDateRange(selectedCategory, startDate, endDate).observe(
                        getViewLifecycleOwner(), expenses -> {
                            adapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            } else {
                expenseViewModel.getExpensesByCategory(selectedCategory).observe(
                        getViewLifecycleOwner(), expenses -> {
                            adapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            }
        }
    }

    private void updateTotalExpenses(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        totalExpensesTextView.setText(String.format(Locale.getDefault(), "Total: ₱%.2f", total));
    }

    private void showAddExpenseDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_expense, null);
        dialog.setContentView(dialogView);

        EditText descriptionEditText = dialogView.findViewById(R.id.description_edit_text);
        EditText amountEditText = dialogView.findViewById(R.id.amount_edit_text);
        Spinner categorySpinner = dialogView.findViewById(R.id.category_spinner);
        Button dateButton = dialogView.findViewById(R.id.date_button);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        // Set hint to show Philippine Peso
        TextView currencyLabel = dialogView.findViewById(R.id.currency_label);
        if (currencyLabel != null) {
            currencyLabel.setText("₱");
        }
        // If there's no currency label, modify the amountEditText hint
        else if (amountEditText.getHint() != null && amountEditText.getHint().toString().contains("$")) {
            amountEditText.setHint(amountEditText.getHint().toString().replace("$", "₱"));
        }

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        dateButton.setText(dateFormat.format(calendar.getTime()));

        dateButton.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                calendar.setTimeInMillis(selection);
                dateButton.setText(dateFormat.format(calendar.getTime()));
            });

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });

        saveButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString().trim();
            String amountStr = amountEditText.getText().toString().trim();

            if (description.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                String category = categorySpinner.getSelectedItem().toString();
                long date = calendar.getTimeInMillis();

                Expense expense = new Expense(0, description, amount, category, date);
                expenseViewModel.insert(expense);

                dialog.dismiss();
                Toast.makeText(requireContext(), "Expense added successfully", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}