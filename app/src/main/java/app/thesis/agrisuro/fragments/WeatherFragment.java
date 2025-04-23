package app.thesis.agrisuro.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.ForecastAdapter;
import app.thesis.agrisuro.models.ForecastItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private TextView temperatureText, conditionText, humidityText, windText, rainText, locationText;
    private ImageView weatherIcon;
    private FusedLocationProviderClient fusedLocationClient;

    // New UI elements
    private TextView textFeelsLike, textPressure, textVisibility, textUvIndex, lastUpdateText;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView forecastRecyclerView;
    private FloatingActionButton locationFab;
    private ForecastAdapter forecastAdapter;
    private List<ForecastItem> forecastItems;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        temperatureText = view.findViewById(R.id.textTemperature);
        conditionText = view.findViewById(R.id.textCondition);
        humidityText = view.findViewById(R.id.textHumidity);
        windText = view.findViewById(R.id.textWind);
        rainText = view.findViewById(R.id.textRain);
        locationText = view.findViewById(R.id.textLocation);
        weatherIcon = view.findViewById(R.id.weatherIcon);

        // Initialize new UI elements
        initializeNewUiElements(view);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        checkLocationPermission();

        return view;
    }

    private void initializeNewUiElements(View view) {
        // Find new UI elements
        textFeelsLike = view.findViewById(R.id.textFeelsLike);
        textPressure = view.findViewById(R.id.textPressure);
        textVisibility = view.findViewById(R.id.textVisibility);
        textUvIndex = view.findViewById(R.id.textUvIndex);
        lastUpdateText = view.findViewById(R.id.lastUpdateText);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        forecastRecyclerView = view.findViewById(R.id.forecastRecyclerView);
        locationFab = view.findViewById(R.id.locationFab);

        // Setup swipe refresh
        swipeRefresh.setOnRefreshListener(() -> {
            checkLocationPermission();
        });

        // Setup location FAB
        locationFab.setOnClickListener(v -> {
            checkLocationPermission();
            Toast.makeText(requireContext(), "Updating location...", Toast.LENGTH_SHORT).show();
        });

        // Setup forecast RecyclerView
        forecastItems = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(forecastItems);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        forecastRecyclerView.setAdapter(forecastAdapter);
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            fetchLocation();
        }
    }

    private void fetchLocation() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            getCityName(lat, lon);  // Get city name from lat/lon
                            getWeatherData(lat, lon);
                            getForecastData(lat, lon); // Get 5-day forecast
                        } else {
                            Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show();
                            if (swipeRefresh != null) {
                                swipeRefresh.setRefreshing(false);
                            }
                        }
                    }
                });
    }

    // Convert latitude and longitude to a city name using Geocoder
    private void getCityName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                if (cityName != null) {
                    locationText.setText(cityName);
                } else {
                    locationText.setText("Unknown");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationText.setText("Location: Error");
        }
    }

    private void getWeatherData(double lat, double lon) {
        new Thread(() -> {
            try {
                String apiKey = "086e611b0cfc168128c076a0a79fae90";
                String urlStr = "https://api.openweathermap.org/data/2.5/weather?lat=" +
                        lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
                URL url = new URL(urlStr);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                reader.close();
                urlConnection.disconnect();

                JSONObject jsonObject = new JSONObject(json.toString());
                JSONObject main = jsonObject.getJSONObject("main");
                JSONObject wind = jsonObject.getJSONObject("wind");
                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

                double tempValue = main.getDouble("temp");
                String temperature = Math.round(tempValue) + "°C";
                String condition = weather.getString("description");
                String humidity = main.getString("humidity") + "%";
                String windSpeed = wind.getString("speed") + " m/s";
                String icon = weather.getString("icon");
                String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                // New data fields
                double feelsLike = main.getDouble("feels_like");
                int pressure = main.getInt("pressure");
                int visibility = jsonObject.has("visibility") ? jsonObject.getInt("visibility") / 1000 : 0; // Convert to km

                // Get current time for last updated
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, MMM dd", Locale.getDefault());
                String currentTime = "Updated: " + sdf.format(new Date());

                requireActivity().runOnUiThread(() -> {
                    temperatureText.setText(temperature);
                    conditionText.setText(capitalizeFirstLetter(condition));
                    humidityText.setText(humidity);
                    windText.setText(windSpeed);

                    // Update new UI elements
                    textFeelsLike.setText("Feels like: " + Math.round(feelsLike) + "°C");
                    textPressure.setText(pressure + " hPa");
                    textVisibility.setText(visibility + " km");
                    textUvIndex.setText("N/A"); // UV index not in this API call
                    lastUpdateText.setText(currentTime);

                    try {
                        String rainChance = "N/A";
                        if (jsonObject.has("rain")) {
                            JSONObject rain = jsonObject.getJSONObject("rain");
                            if (rain.has("1h")) {
                                rainChance = rain.getDouble("1h") + " mm";
                            }
                        }
                        rainText.setText(rainChance);
                    } catch (Exception e) {
                        rainText.setText("N/A");
                    }

                    Glide.with(requireContext()).load(iconUrl).into(weatherIcon);

                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void getForecastData(double lat, double lon) {
        new Thread(() -> {
            try {
                String apiKey = "086e611b0cfc168128c076a0a79fae90";
                String urlStr = "https://api.openweathermap.org/data/2.5/forecast?lat=" +
                        lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
                URL url = new URL(urlStr);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                reader.close();
                urlConnection.disconnect();

                JSONObject jsonObject = new JSONObject(json.toString());
                JSONArray forecastList = jsonObject.getJSONArray("list");

                // Clear previous forecast data
                forecastItems.clear();

                // Process forecast data (one item per day, not every 3 hours)
                String currentDate = "";
                for (int i = 0; i < forecastList.length(); i++) {
                    JSONObject forecast = forecastList.getJSONObject(i);
                    String dateText = forecast.getString("dt_txt").split(" ")[0]; // Get date part only

                    // Only add one forecast per day
                    if (!dateText.equals(currentDate) && forecastItems.size() < 5) {
                        currentDate = dateText;

                        JSONObject main = forecast.getJSONObject("main");
                        JSONObject weather = forecast.getJSONArray("weather").getJSONObject(0);

                        double tempMax = main.getDouble("temp_max");
                        double tempMin = main.getDouble("temp_min");
                        String description = weather.getString("description");
                        String icon = weather.getString("icon");
                        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                        // Format date for display
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, MMM dd", Locale.US);
                        Date date = inputFormat.parse(dateText);
                        String formattedDate = outputFormat.format(date);

                        ForecastItem item = new ForecastItem(
                                formattedDate,
                                Math.round(tempMax) + "°",
                                Math.round(tempMin) + "°",
                                capitalizeFirstLetter(description),
                                iconUrl
                        );

                        forecastItems.add(item);
                    }
                }

                requireActivity().runOnUiThread(() -> {
                    forecastAdapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Error fetching forecast data", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }
    }
}