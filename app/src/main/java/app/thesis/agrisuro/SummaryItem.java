package app.thesis.agrisuro;

public class SummaryItem {
    private String name;
    private double amount;

    public SummaryItem(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }
}