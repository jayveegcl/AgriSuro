package app.thesis.agrisuro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FinanceTrackerFragment extends Fragment {

    private FinanceViewModel financeViewModel;
    private ExpenseAdapter expenseAdapter;
    private IncomeAdapter incomeAdapter;
    private TextView totalAmountTextView;
    private Spinner categoryFilterSpinner;
    private TextView selectedDateRangeTextView;
    private Button filterButton;
    private long startDate = 0;
    private long endDate = 0;
    private RecyclerView recyclerView;
    private List<Expense> currentExpenses = new ArrayList<>();
    private List<Income> currentIncomes = new ArrayList<>();
    private TabLayout tabLayout;
    private boolean isShowingExpenses = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financeViewModel = new ViewModelProvider(requireActivity()).get(FinanceViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finance_tracker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.finance_recycler_view);
        FloatingActionButton addItemFab = view.findViewById(R.id.add_item_fab);
        totalAmountTextView = view.findViewById(R.id.total_amount_text_view);
        categoryFilterSpinner = view.findViewById(R.id.category_filter_spinner);
        selectedDateRangeTextView = view.findViewById(R.id.selected_date_range_text_view);
        filterButton = view.findViewById(R.id.filter_button);
        Button dateRangeButton = view.findViewById(R.id.date_range_button);
        Button summaryButton = view.findViewById(R.id.summary_button);
        tabLayout = view.findViewById(R.id.tab_layout);

        // Set up initial spinner appearance
        setupSpinnerStyle(categoryFilterSpinner);

        // Set up TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Expenses"));
        tabLayout.addTab(tabLayout.newTab().setText("Income"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    // Show expenses
                    isShowingExpenses = true;
                    expenseAdapter.setExpenses(currentExpenses);
                    recyclerView.setAdapter(expenseAdapter);
                    updateTotalExpenses(currentExpenses);

                    // Create and set adapter with better styling
                    ArrayAdapter<CharSequence> expenseAdapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.expense_categories,
                            android.R.layout.simple_spinner_item);
                    expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categoryFilterSpinner.setAdapter(expenseAdapter);

                    // Apply styling to spinner
                    setupSpinnerStyle(categoryFilterSpinner);
                } else {
                    // Show incomes
                    isShowingExpenses = false;
                    incomeAdapter.setIncomes(currentIncomes);
                    recyclerView.setAdapter(incomeAdapter);
                    updateTotalIncomes(currentIncomes);

                    // Create and set adapter with better styling
                    ArrayAdapter<CharSequence> incomeAdapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.income_sources,
                            android.R.layout.simple_spinner_item);
                    incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categoryFilterSpinner.setAdapter(incomeAdapter);

                    // Apply styling to spinner
                    setupSpinnerStyle(categoryFilterSpinner);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        expenseAdapter = new ExpenseAdapter();
        incomeAdapter = new IncomeAdapter();
        recyclerView.setAdapter(expenseAdapter); // Default to expenses

        // Initialize spinner with expenses categories (default view)
        ArrayAdapter<CharSequence> initialAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.expense_categories,
                android.R.layout.simple_spinner_item);
        initialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryFilterSpinner.setAdapter(initialAdapter);
        setupSpinnerStyle(categoryFilterSpinner);

        // Set up item click listeners for deletion
        expenseAdapter.setOnItemClickListener(expense -> showDeleteExpenseConfirmationDialog(expense));
        incomeAdapter.setOnItemClickListener(income -> showDeleteIncomeConfirmationDialog(income));

        // Observe expenses and incomes from ViewModel
        financeViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            currentExpenses = expenses;
            if (isShowingExpenses) {
                expenseAdapter.setExpenses(expenses);
                updateTotalExpenses(expenses);
            }
        });

        financeViewModel.getAllIncomes().observe(getViewLifecycleOwner(), incomes -> {
            currentIncomes = incomes;
            if (!isShowingExpenses) {
                incomeAdapter.setIncomes(incomes);
                updateTotalIncomes(incomes);
            }
        });

        // Set up date range picker
        dateRangeButton.setOnClickListener(v -> showDateRangePicker());

        // Set up filter button
        filterButton.setOnClickListener(v -> applyFilters());

        // Set up add item button
        addItemFab.setOnClickListener(v -> {
            if (isShowingExpenses) {
                showAddExpenseDialog();
            } else {
                showAddIncomeDialog();
            }
        });

        // Set up summary button
        summaryButton.setOnClickListener(v -> showFinancialSummary());
    }

    // Helper method to set up spinner styling
    private void setupSpinnerStyle(Spinner spinner) {
        ViewGroup.LayoutParams params = spinner.getLayoutParams();
        // Make the spinner taller (56dp converted to pixels)
        params.height = (int) (56 * getResources().getDisplayMetrics().density);
        spinner.setLayoutParams(params);

        // Set a better background with more padding
        spinner.setBackgroundResource(R.drawable.spinner_background);

        // Add padding inside the spinner
        spinner.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                spinner.getPaddingTop(),
                spinner.getPaddingRight(),
                spinner.getPaddingBottom());
    }

    private void showDeleteExpenseConfirmationDialog(Expense expense) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Expense");
        builder.setMessage("Are you sure you want to delete this expense?\n\n" +
                expense.getDescription() + " - ₱" + String.format(Locale.getDefault(), "%.2f", expense.getAmount()));

        builder.setPositiveButton("Delete", (dialog, which) -> {
            financeViewModel.deleteExpense(expense);
            Toast.makeText(requireContext(), "Expense deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showDeleteIncomeConfirmationDialog(Income income) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Income");
        builder.setMessage("Are you sure you want to delete this income?\n\n" +
                income.getDescription() + " - ₱" + String.format(Locale.getDefault(), "%.2f", income.getAmount()));

        builder.setPositiveButton("Delete", (dialog, which) -> {
            financeViewModel.deleteIncome(income);
            Toast.makeText(requireContext(), "Income deleted", Toast.LENGTH_SHORT).show();
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
        String selectedCategoryOrSource = categoryFilterSpinner.getSelectedItem().toString();

        if (isShowingExpenses) {
            applyExpenseFilters(selectedCategoryOrSource);
        } else {
            applyIncomeFilters(selectedCategoryOrSource);
        }
    }

    private void applyExpenseFilters(String selectedCategory) {
        if ("All Categories".equals(selectedCategory)) {
            if (startDate > 0 && endDate > 0) {
                financeViewModel.getExpensesByDateRange(startDate, endDate).observe(
                        getViewLifecycleOwner(), expenses -> {
                            currentExpenses = expenses;
                            expenseAdapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            } else {
                financeViewModel.getAllExpenses().observe(
                        getViewLifecycleOwner(), expenses -> {
                            currentExpenses = expenses;
                            expenseAdapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            }
        } else {
            if (startDate > 0 && endDate > 0) {
                financeViewModel.getExpensesByCategoryAndDateRange(selectedCategory, startDate, endDate).observe(
                        getViewLifecycleOwner(), expenses -> {
                            currentExpenses = expenses;
                            expenseAdapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            } else {
                financeViewModel.getExpensesByCategory(selectedCategory).observe(
                        getViewLifecycleOwner(), expenses -> {
                            currentExpenses = expenses;
                            expenseAdapter.setExpenses(expenses);
                            updateTotalExpenses(expenses);
                        });
            }
        }
    }

    private void applyIncomeFilters(String selectedSource) {
        if ("All Sources".equals(selectedSource)) {
            if (startDate > 0 && endDate > 0) {
                financeViewModel.getIncomesByDateRange(startDate, endDate).observe(
                        getViewLifecycleOwner(), incomes -> {
                            currentIncomes = incomes;
                            incomeAdapter.setIncomes(incomes);
                            updateTotalIncomes(incomes);
                        });
            } else {
                financeViewModel.getAllIncomes().observe(
                        getViewLifecycleOwner(), incomes -> {
                            currentIncomes = incomes;
                            incomeAdapter.setIncomes(incomes);
                            updateTotalIncomes(incomes);
                        });
            }
        } else {
            if (startDate > 0 && endDate > 0) {
                financeViewModel.getIncomesBySourceAndDateRange(selectedSource, startDate, endDate).observe(
                        getViewLifecycleOwner(), incomes -> {
                            currentIncomes = incomes;
                            incomeAdapter.setIncomes(incomes);
                            updateTotalIncomes(incomes);
                        });
            } else {
                financeViewModel.getIncomesBySource(selectedSource).observe(
                        getViewLifecycleOwner(), incomes -> {
                            currentIncomes = incomes;
                            incomeAdapter.setIncomes(incomes);
                            updateTotalIncomes(incomes);
                        });
            }
        }
    }

    private void updateTotalExpenses(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        totalAmountTextView.setText(String.format(Locale.getDefault(), "Total Expenses: ₱%.2f", total));
    }

    private void updateTotalIncomes(List<Income> incomes) {
        double total = 0;
        for (Income income : incomes) {
            total += income.getAmount();
        }
        totalAmountTextView.setText(String.format(Locale.getDefault(), "Total Income: ₱%.2f", total));
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

        // Make the dialog spinner larger too
        setupSpinnerStyle(categorySpinner);

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
                financeViewModel.insertExpense(expense);

                dialog.dismiss();
                Toast.makeText(requireContext(), "Expense added successfully", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showAddIncomeDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_income, null);
        dialog.setContentView(dialogView);

        EditText descriptionEditText = dialogView.findViewById(R.id.description_edit_text);
        EditText amountEditText = dialogView.findViewById(R.id.amount_edit_text);
        Spinner sourceSpinner = dialogView.findViewById(R.id.source_spinner);
        Button dateButton = dialogView.findViewById(R.id.date_button);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        // Make the dialog spinner larger too
        setupSpinnerStyle(sourceSpinner);

        // Set up date selection
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
                String source = sourceSpinner.getSelectedItem().toString();
                long date = calendar.getTimeInMillis();

                Income income = new Income(0, description, amount, source, date);
                financeViewModel.insertIncome(income);

                dialog.dismiss();
                Toast.makeText(requireContext(), "Income added successfully", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showFinancialSummary() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_financial_summary, null);
        dialog.setContentView(dialogView);

        // Set up views
        TextView totalIncomeTextView = dialogView.findViewById(R.id.total_income_value);
        TextView totalExpensesTextView = dialogView.findViewById(R.id.total_expenses_value);
        TextView netIncomeTextView = dialogView.findViewById(R.id.net_income_value);
        RecyclerView categorySummaryRecyclerView = dialogView.findViewById(R.id.category_summary_recycler_view);
        RecyclerView sourceSummaryRecyclerView = dialogView.findViewById(R.id.source_summary_recycler_view);

        // Calculate totals
        double totalIncome = calculateTotalIncome(currentIncomes);
        double totalExpenses = calculateTotalExpenses(currentExpenses);
        double netIncome = totalIncome - totalExpenses;

        // Set values
        totalIncomeTextView.setText(String.format(Locale.getDefault(), "₱%.2f", totalIncome));
        totalExpensesTextView.setText(String.format(Locale.getDefault(), "₱%.2f", totalExpenses));

        // Set net income with color (green for positive, red for negative)
        netIncomeTextView.setText(String.format(Locale.getDefault(), "₱%.2f", netIncome));
        if (netIncome >= 0) {
            netIncomeTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            netIncomeTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Set up category summary
        categorySummaryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<SummaryItem> categoryItems = getCategorySummary(currentExpenses);
        SummaryAdapter categoryAdapter = new SummaryAdapter(categoryItems);
        categorySummaryRecyclerView.setAdapter(categoryAdapter);

        // Set up source summary
        sourceSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<SummaryItem> sourceItems = getSourceSummary(currentIncomes);
        SummaryAdapter sourceAdapter = new SummaryAdapter(sourceItems);
        sourceSummaryRecyclerView.setAdapter(sourceAdapter);

        dialog.show();
    }

    private double calculateTotalIncome(List<Income> incomes) {
        double total = 0;
        for (Income income : incomes) {
            total += income.getAmount();
        }
        return total;
    }

    private double calculateTotalExpenses(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    private List<SummaryItem> getCategorySummary(List<Expense> expenses) {
        Map<String, Double> categoryTotals = new HashMap<>();

        // Calculate totals by category
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double currentTotal = categoryTotals.getOrDefault(category, 0.0);
            categoryTotals.put(category, currentTotal + expense.getAmount());
        }

        // Convert to list of SummaryItems
        List<SummaryItem> items = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            items.add(new SummaryItem(entry.getKey(), entry.getValue()));
        }

        return items;
    }

    private List<SummaryItem> getSourceSummary(List<Income> incomes) {
        Map<String, Double> sourceTotals = new HashMap<>();

        // Calculate totals by source
        for (Income income : incomes) {
            String source = income.getSource();
            double currentTotal = sourceTotals.getOrDefault(source, 0.0);
            sourceTotals.put(source, currentTotal + income.getAmount());
        }

        // Convert to list of SummaryItems
        List<SummaryItem> items = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sourceTotals.entrySet()) {
            items.add(new SummaryItem(entry.getKey(), entry.getValue()));
        }

        return items;
    }
}