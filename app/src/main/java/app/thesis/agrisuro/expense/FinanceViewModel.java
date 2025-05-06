package app.thesis.agrisuro.expense;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FinanceViewModel extends AndroidViewModel {
    private ExpenseRepository expenseRepository;
    private IncomeRepository incomeRepository;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<List<Income>> allIncomes;

    public FinanceViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        incomeRepository = new IncomeRepository(application);
        allExpenses = expenseRepository.getAllExpenses();
        allIncomes = incomeRepository.getAllIncomes();
    }

    // Expense methods
    public void insertExpense(Expense expense) {
        expenseRepository.insert(expense);
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return expenseRepository.getExpensesByCategory(category);
    }

    public LiveData<List<Expense>> getExpensesByDateRange(long startDate, long endDate) {
        return expenseRepository.getExpensesByDateRange(startDate, endDate);
    }

    public LiveData<List<Expense>> getExpensesByCategoryAndDateRange(String category, long startDate, long endDate) {
        return expenseRepository.getExpensesByCategoryAndDateRange(category, startDate, endDate);
    }

    // Income methods
    public void insertIncome(Income income) {
        incomeRepository.insert(income);
    }

    public void deleteIncome(Income income) {
        incomeRepository.delete(income);
    }

    public LiveData<List<Income>> getAllIncomes() {
        return allIncomes;
    }

    public LiveData<List<Income>> getIncomesBySource(String source) {
        return incomeRepository.getIncomesBySource(source);
    }

    public LiveData<List<Income>> getIncomesByDateRange(long startDate, long endDate) {
        return incomeRepository.getIncomesByDateRange(startDate, endDate);
    }

    public LiveData<List<Income>> getIncomesBySourceAndDateRange(String source, long startDate, long endDate) {
        return incomeRepository.getIncomesBySourceAndDateRange(source, startDate, endDate);
    }
}