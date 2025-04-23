package app.thesis.agrisuro.models;

public class ForecastItem {
    private String date;
    private String maxTemp;
    private String minTemp;
    private String description;
    private String iconUrl;

    public ForecastItem(String date, String maxTemp, String minTemp, String description, String iconUrl) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public String getDate() {
        return date;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}