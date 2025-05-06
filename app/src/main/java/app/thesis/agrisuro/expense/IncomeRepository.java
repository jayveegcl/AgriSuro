package app.thesis.agrisuro.expense;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncomeRepository {
    private IncomeDao incomeDao;
    private LiveData<List<Income>> allIncomes;
    private ExecutorService executorService;

    public IncomeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        incomeDao = database.incomeDao();
        allIncomes = incomeDao.getAllIncomes();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Income income) {
        executorService.execute(() -> incomeDao.insert(income));
    }

    public void delete(Income income) {
        executorService.execute(() -> incomeDao.delete(income));
    }

    public LiveData<List<Income>> getAllIncomes() {
        return allIncomes;
    }

    public LiveData<List<Income>> getIncomesBySource(String source) {
        return incomeDao.getIncomesBySource(source);
    }

    public LiveData<List<Income>> getIncomesByDateRange(long startDate, long endDate) {
        return incomeDao.getIncomesByDateRange(startDate, endDate);
    }

    public LiveData<List<Income>> getIncomesBySourceAndDateRange(String source, long startDate, long endDate) {
        return incomeDao.getIncomesBySourceAndDateRange(source, startDate, endDate);
    }
}