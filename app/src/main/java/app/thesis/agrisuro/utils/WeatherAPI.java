package app.thesis.agrisuro.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import app.thesis.agrisuro.models.ForecastDay;
import app.thesis.agrisuro.models.PlantingRecommendation;
import app.thesis.agrisuro.models.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class for fetching weather data from a weather API
 */
public class WeatherAPI {
    private static final String TAG = "WeatherAPI";
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your actual API key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private final ExecutorService executorService;
    private final Handler mainHandler;

    public WeatherAPI() {
        executorService = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Interface for weather data callbacks
     */
    public interface WeatherCallback {
        void onWeatherDataReceived(WeatherData weatherData);
        void onError(String errorMessage);
    }

    /**
     * Fetches current weather and forecast data for the given location
     *
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @param callback Callback to handle the response
     */
    public void fetchWeatherData(double latitude, double longitude, WeatherCallback callback) {
        executorService.execute(() -> {
            try {
                // For demonstration purposes, we'll use mock data instead of actual API calls
                // In a real app, you would make HTTP requests to the weather API
                WeatherData mockWeatherData = getMockWeatherData(latitude, longitude);

                // Return the data on the main thread
                mainHandler.post(() -> callback.onWeatherDataReceived(mockWeatherData));
            } catch (Exception e) {
                Log.e(TAG, "Error fetching weather data", e);
                mainHandler.post(() -> callback.onError("Failed to fetch weather data: " + e.getMessage()));
            }
        });
    }

    /**
     * Creates mock weather data for demonstration purposes
     * In a real app, this would parse JSON from the API response
     */
    private WeatherData getMockWeatherData(double latitude, double longitude) {
        // Create mock current weather data
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(28.5f);
        weatherData.setFeelsLike(30.2f);
        weatherData.setHumidity(65);
        weatherData.setWindSpeed(12.5f);
        weatherData.setDescription("Partly Cloudy");
        weatherData.setIconCode("02d");
        weatherData.setLocation("Manila, Philippines");
        weatherData.setLatitude(latitude);
        weatherData.setLongitude(longitude);

        // Create mock forecast data
        List<ForecastDay> forecastList = new ArrayList<>();

        ForecastDay day1 = new ForecastDay();
        day1.setDate("Tomorrow");
        day1.setMaxTemp(29.5f);
        day1.setMinTemp(24.0f);
        day1.setIconCode("01d");
        day1.setDescription("Sunny");
        forecastList.add(day1);

        ForecastDay day2 = new ForecastDay();
        day2.setDate("Wednesday");
        day2.setMaxTemp(30.2f);
        day2.setMinTemp(25.1f);
        day2.setIconCode("10d");
        day2.setDescription("Light Rain");
        forecastList.add(day2);

        ForecastDay day3 = new ForecastDay();
        day3.setDate("Thursday");
        day3.setMaxTemp(27.8f);
        day3.setMinTemp(23.5f);
        day3.setIconCode("11d");
        day3.setDescription("Thunderstorm");
        forecastList.add(day3);

        weatherData.setForecast(forecastList);

        // Create mock planting recommendations
        List<PlantingRecommendation> recommendations = new ArrayList<>();

        PlantingRecommendation rec1 = new PlantingRecommendation();
        rec1.setCropName("Rice");
        rec1.setRecommendation("Good conditions for planting in the next 3 days");
        rec1.setConfidence(85);
        recommendations.add(rec1);

        PlantingRecommendation rec2 = new PlantingRecommendation();
        rec2.setCropName("Corn");
        rec2.setRecommendation("Wait for drier conditions next week");
        rec2.setConfidence(70);
        recommendations.add(rec2);

        weatherData.setPlantingRecommendations(recommendations);

        return weatherData;
    }

    /**
     * Method to fetch actual weather data from OpenWeatherMap API
     * This would be used in a real implementation instead of mock data
     */
    private String fetchDataFromApi(String endpoint, double latitude, double longitude) throws IOException {
        String urlString = BASE_URL + endpoint + "?lat=" + latitude + "&lon=" + longitude +
                "&units=metric&appid=" + API_KEY;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            connection.disconnect();
        }

        return response.toString();
    }

    /**
     * Parses weather data from JSON response
     * This would be used in a real implementation
     */
    private WeatherData parseWeatherData(String currentWeatherJson, String forecastJson) throws JSONException {
        // This is a placeholder for actual JSON parsing logic
        // In a real app, you would parse the JSON responses from the API
        return new WeatherData();
    }
}
