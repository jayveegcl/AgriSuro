package app.thesis.agrisuro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return repository.getExpensesByCategory(category);
    }

    public LiveData<List<Expense>> getExpensesByDateRange(long startDate, long endDate) {
        return repository.getExpensesByDateRange(startDate, endDate);
    }

    public LiveData<List<Expense>> getExpensesByCategoryAndDateRange(String category, long startDate, long endDate) {
        return repository.getExpensesByCategoryAndDateRange(category, startDate, endDate);
    }
}