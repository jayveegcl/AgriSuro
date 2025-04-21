package app.thesis.agrisuro.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing weather data for the AgriSuro application.
 * Contains current weather conditions and forecast information.
 */
public class WeatherData {
    private String location;
    private String date;
    private double temperature;
    private String weatherCondition;
    private int humidity;
    private double rainfall;
    private double windSpeed;
    private String windDirection;
    private List<ForecastDay> forecast;
    private List<PlantingRecommendation> plantingRecommendations;

    public WeatherData() {
        this.forecast = new ArrayList<>();
        this.plantingRecommendations = new ArrayList<>();
    }

    public WeatherData(String location, String date, double temperature, String weatherCondition,
                       int humidity, double rainfall, double windSpeed, String windDirection) {
        this.location = location;
        this.date = date;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.humidity = humidity;
        this.rainfall = rainfall;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.forecast = new ArrayList<>();
        this.plantingRecommendations = new ArrayList<>();
    }

    // Getters and Setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getRainfall() {
        return rainfall;
    }

    public void setRainfall(double rainfall) {
        this.rainfall = rainfall;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public List<ForecastDay> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastDay> forecast) {
        this.forecast = forecast;
    }

    public void addForecastDay(ForecastDay forecastDay) {
        this.forecast.add(forecastDay);
    }

    public List<PlantingRecommendation> getPlantingRecommendations() {
        return plantingRecommendations;
    }

    public void setPlantingRecommendations(List<PlantingRecommendation> plantingRecommendations) {
        this.plantingRecommendations = plantingRecommendations;
    }

    public void addPlantingRecommendation(PlantingRecommendation recommendation) {
        this.plantingRecommendations.add(recommendation);
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", temperature=" + temperature +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", humidity=" + humidity +
                ", rainfall=" + rainfall +
                ", windSpeed=" + windSpeed +
                ", windDirection='" + windDirection + '\'' +
                ", forecast=" + forecast.size() +
                ", plantingRecommendations=" + plantingRecommendations.size() +
                '}';
    }
}
