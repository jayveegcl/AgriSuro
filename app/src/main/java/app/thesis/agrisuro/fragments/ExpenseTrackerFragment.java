package app.thesis.agrisuro.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.ExpenseAdapter;
import app.thesis.agrisuro.adapter.CategoryExpenseAdapter;
import app.thesis.agrisuro.models.Expense;
import app.thesis.agrisuro.models.CategoryExpense;

public class ExpenseTrackerFragment extends Fragment {

    private TabLayout tabLayout;
    private View dashboardView;
    private View addExpenseView;
    private View reportsView;
    private RecyclerView recentExpensesRecyclerView;
    private RecyclerView categoryExpensesRecyclerView;
    private Button addExpenseButton;
    private Button saveExpenseButton;
    private Button cancelButton;
    private TextInputEditText amountInput;
    private TextInputEditText categoryInput;
    private TextInputEditText dateInput;
    private TextInputEditText descriptionInput;

    private List<Expense> expenses;
    private ExpenseAdapter expenseAdapter;
    private CategoryExpenseAdapter categoryAdapter;
    private Calendar selectedDate = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_tracker, container, false);

        // Initialize UI components
        tabLayout = view.findViewById(R.id.expense_tabs);
        dashboardView = view.findViewById(R.id.dashboard_view);
        addExpenseView = view.findViewById(R.id.add_expense_view);
        reportsView = view.findViewById(R.id.reports_view);
        recentExpensesRecyclerView = view.findViewById(R.id.recent_expenses_recycler_view);
        categoryExpensesRecyclerView = view.findViewById(R.id.category_expenses_recycler_view);
        addExpenseButton = view.findViewById(R.id.add_expense_button);
        saveExpenseButton = view.findViewById(R.id.save_expense_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        amountInput = view.findViewById(R.id.amount_input);
        categoryInput = view.findViewById(R.id.category_input);
        dateInput = view.findViewById(R.id.date_input);
        descriptionInput = view.findViewById(R.id.description_input);

        // Initialize data
        expenses = getMockExpenses();

        // Set up date picker
        dateInput.setOnClickListener(v -> showDatePickerDialog());
        dateInput.setText(dateFormat.format(selectedDate.getTime()));

        // Set up tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateViewVisibility(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set up RecyclerViews
        recentExpensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseAdapter = new ExpenseAdapter(expenses.subList(0, Math.min(5, expenses.size())));
        recentExpensesRecyclerView.setAdapter(expenseAdapter);

        categoryExpensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new CategoryExpenseAdapter(getCategoryExpenses());
        categoryExpensesRecyclerView.setAdapter(categoryAdapter);

        // Set up buttons
        addExpenseButton.setOnClickListener(v -> {
            tabLayout.getTabAt(1).select();
            clearInputFields();
        });

        saveExpenseButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveExpense();
                tabLayout.getTabAt(0).select();
            }
        });

        cancelButton.setOnClickListener(v -> {
            tabLayout.getTabAt(0).select();
        });

        // Set initial view
        updateViewVisibility(0);

        return view;
    }

    private void updateViewVisibility(int tabPosition) {
        dashboardView.setVisibility(tabPosition == 0 ? View.VISIBLE : View.GONE);
        addExpenseView.setVisibility(tabPosition == 1 ? View.VISIBLE : View.GONE);
        reportsView.setVisibility(tabPosition == 2 ? View.VISIBLE : View.GONE);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateInput.setText(dateFormat.format(selectedDate.getTime()));
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (amountInput.getText().toString().trim().isEmpty()) {
            amountInput.setError("Amount is required");
            isValid = false;
        }

        if (categoryInput.getText().toString().trim().isEmpty()) {
            categoryInput.setError("Category is required");
            isValid = false;
        }

        return isValid;
    }

    private void saveExpense() {
        try {
            int amount = Integer.parseInt(amountInput.getText().toString().trim());
            String category = categoryInput.getText().toString().trim();
            Date date = selectedDate.getTime();
            String description = descriptionInput.getText().toString().trim();

            // Generate a new ID (in a real app, this would be handled by a database)
            String id = "exp_" + System.currentTimeMillis();

            // Create new expense and add it to the list
            Expense newExpense = new Expense(id, date, amount, category, description);
            expenses.add(0, newExpense); // Add to the beginning of the list

            // Update the adapters
            updateAdapters();

            Toast.makeText(getContext(), "Expense added successfully!", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } catch (NumberFormatException e) {
            amountInput.setError("Please enter a valid amount");
        }
    }

    private void clearInputFields() {
        amountInput.setText("");
        categoryInput.setText("");
        selectedDate = Calendar.getInstance();
        dateInput.setText(dateFormat.format(selectedDate.getTime()));
        descriptionInput.setText("");
        amountInput.setError(null);
        categoryInput.setError(null);
    }

    private void updateAdapters() {
        // Update recent expenses adapter
        expenseAdapter = new ExpenseAdapter(expenses.subList(0, Math.min(5, expenses.size())));
        recentExpensesRecyclerView.setAdapter(expenseAdapter);

        // Update category adapter
        categoryAdapter = new CategoryExpenseAdapter(getCategoryExpenses());
        categoryExpensesRecyclerView.setAdapter(categoryAdapter);
    }

    private List<Expense> getMockExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(new Expense("1", new Date(2023, 5, 15), 2500, "Seeds", "Rice seeds for north field"));
        expenseList.add(new Expense("2", new Date(2023, 5, 20), 1800, "Fertilizer", "Nitrogen fertilizer"));
        expenseList.add(new Expense("3", new Date(2023, 6, 5), 3200, "Pesticide", "Insecticide for pest control"));
        expenseList.add(new Expense("4", new Date(2023, 6, 15), 5000, "Equipment", "Repair for water pump"));
        expenseList.add(new Expense("5", new Date(2023, 7, 1), 1200, "Labor", "Hired help for planting"));
        return expenseList;
    }

    private List<CategoryExpense> getCategoryExpenses() {
        Map<String, Integer> categoryTotals = new HashMap<>();
        int totalExpense = 0;

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            int amount = expense.getAmount();
            totalExpense += amount;

            if (categoryTotals.containsKey(category)) {
                categoryTotals.put(category, categoryTotals.get(category) + amount);
            } else {
                categoryTotals.put(category, amount);
            }
        }

        List<CategoryExpense> categoryExpenses = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryTotals.entrySet()) {
            categoryExpenses.add(new CategoryExpense(
                    entry.getKey(),
                    entry.getValue(),
                    (float) entry.getValue() / totalExpense
            ));
        }

        return categoryExpenses;
    }
}