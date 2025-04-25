package app.thesis.agrisuro;

public class RiceRecommendationItem {
    private String date;
    private String recommendation;

    public RiceRecommendationItem(String date, String recommendation) {
        this.date = date;
        this.recommendation = recommendation;
    }

    public String getDate() {
        return date;
    }

    public String getRecommendation() {
        return recommendation;
    }
}
