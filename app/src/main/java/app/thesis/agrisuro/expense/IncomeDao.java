package app.thesis.agrisuro.expense;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IncomeDao {
    @Insert
    void insert(Income income);

    @Delete
    void delete(Income income);

    @Query("SELECT * FROM incomes ORDER BY date DESC")
    LiveData<List<Income>> getAllIncomes();

    @Query("SELECT * FROM incomes WHERE source = :source ORDER BY date DESC")
    LiveData<List<Income>> getIncomesBySource(String source);

    @Query("SELECT * FROM incomes WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Income>> getIncomesByDateRange(long startDate, long endDate);

    @Query("SELECT * FROM incomes WHERE source = :source AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Income>> getIncomesBySourceAndDateRange(String source, long startDate, long endDate);
}