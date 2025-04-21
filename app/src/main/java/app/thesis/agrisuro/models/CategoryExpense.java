package app.thesis.agrisuro.models;

public class CategoryExpense {
    private String category;
    private int total;
    private float percentage;

    public CategoryExpense(String category, int total, float percentage) {
        this.category = category;
        this.total = total;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public int getTotal() {
        return total;
    }

    public float getPercentage() {
        return percentage;
    }
}

