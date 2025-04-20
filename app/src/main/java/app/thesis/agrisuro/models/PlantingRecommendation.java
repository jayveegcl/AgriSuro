package app.thesis.agrisuro.models;

public class PlantingRecommendation {
    private String crop;
    private String recommendation;
    private String optimalTimeframe;
    private int suitabilityScore;

    public PlantingRecommendation(String crop, String recommendation, String optimalTimeframe, int suitabilityScore) {
        this.crop = crop;
        this.recommendation = recommendation;
        this.optimalTimeframe = optimalTimeframe;
        this.suitabilityScore = suitabilityScore;
    }

    public String getCrop() {
        return crop;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getOptimalTimeframe() {
        return optimalTimeframe;
    }

    public int getSuitabilityScore() {
        return suitabilityScore;
    }
}
