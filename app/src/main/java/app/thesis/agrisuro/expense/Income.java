package app.thesis.agrisuro.expense;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "incomes")
public class Income {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private double amount;
    private String source;
    private long date;

    public Income(int id, String description, double amount, String source, long date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.source = source;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}