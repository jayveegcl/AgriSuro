package app.thesis.agrisuro.models;

import java.util.Date;

public class Expense {
    private String id;
    private Date date;
    private int amount;
    private String category;
    private String description;
    private String receipt;

    public Expense(String id, Date date, int amount, String category, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public Expense(String id, Date date, int amount, String category, String description, String receipt) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.receipt = receipt;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getReceipt() {
        return receipt;
    }
}
