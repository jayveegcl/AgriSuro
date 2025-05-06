package app.thesis.agrisuro.expense;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;
    private ExecutorService executorService;

    public ExpenseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        expenseDao = database.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Expense expense) {
        executorService.execute(() -> expenseDao.insert(expense));
    }

    public void delete(Expense expense) {
        executorService.execute(() -> expenseDao.delete(expense));
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return expenseDao.getExpensesByCategory(category);
    }

    public LiveData<List<Expense>> getExpensesByDateRange(long startDate, long endDate) {
        return expenseDao.getExpensesByDateRange(startDate, endDate);
    }

    public LiveData<List<Expense>> getExpensesByCategoryAndDateRange(String category, long startDate, long endDate) {
        return expenseDao.getExpensesByCategoryAndDateRange(category, startDate, endDate);
    }
}