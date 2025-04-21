package app.thesis.agrisuro.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.ForecastAdapter;
import app.thesis.agrisuro.adapter.PlantingRecommendationAdapter;
import app.thesis.agrisuro.models.ForecastDay;
import app.thesis.agrisuro.models.PlantingRecommendation;
import app.thesis.agrisuro.utils.WeatherUtils;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherFragment extends Fragment implements LocationListener {

    // Weather widget views
    private TextView tvLocation;
    private Button btnLocation;
    private TextView tvDate;
    private TextView tvTemperature;
    private TextView tvWeatherCondition;
    private ImageView ivWeatherIcon;
    private TextView tvHumidity;
    private TextView tvRainfall;
    private TextView tvWind;
    private RecyclerView rvForecast;
    private RecyclerView rvPlantingRecommendations;
    private Button btnViewAllWeather;
    private ProgressBar progressBar;

    // Original fragment views
    private TabLayout tabLayout;
    private TextView locationText;
    private Button locationButton;
    private Button refreshButton;
    private TextView currentTempText;
    private TextView conditionText;
    private TextView humidityText;
    private TextView windText;
    private TextView rainText;
    private RecyclerView forecastRecyclerView;
    private RecyclerView recommendationsRecyclerView;
    private View currentWeatherView;
    private View plantingGuideView;

    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final long MIN_TIME_BETWEEN_UPDATES = 60000; // 1 minute
    private static final float MIN_DISTANCE_CHANGE = 1000; // 1 kilometer
    private boolean isLocationRequested = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Initialize UI components for the fragment
        tabLayout = view.findViewById(R.id.weather_tabs);
        locationText = view.findViewById(R.id.location_text);
        locationButton = view.findViewById(R.id.location_button);
        refreshButton = view.findViewById(R.id.refresh_button);
        currentTempText = view.findViewById(R.id.current_temp);
        conditionText = view.findViewById(R.id.condition_text);
        humidityText = view.findViewById(R.id.humidity_text);
        windText = view.findViewById(R.id.wind_text);
        rainText = view.findViewById(R.id.rain_text);
        forecastRecyclerView = view.findViewById(R.id.forecast_recycler_view);
        recommendationsRecyclerView = view.findViewById(R.id.recommendations_recycler_view);
        currentWeatherView = view.findViewById(R.id.current_weather_view);
        plantingGuideView = view.findViewById(R.id.planting_guide_view);

        // Initialize the weather widget views
        View weatherWidgetView = view.findViewById(R.id.weather_widget);
        if (weatherWidgetView != null) {
            initializeWeatherWidgetViews(weatherWidgetView);
        }

        // Set up location button
        locationButton.setOnClickListener(v -> {
            requestLocationUpdates();
        });

        // Set up refresh button
        refreshButton.setOnClickListener(v -> {
            // Simulate refresh
            refreshButton.setEnabled(false);
            // In a real app, this would call a weather API
            new android.os.Handler().postDelayed(() -> {
                refreshButton.setEnabled(true);
                Toast.makeText(getContext(), "Weather data refreshed", Toast.LENGTH_SHORT).show();
            }, 1000);
        });

        // Set up tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    currentWeatherView.setVisibility(View.VISIBLE);
                    plantingGuideView.setVisibility(View.GONE);
                } else {
                    currentWeatherView.setVisibility(View.GONE);
                    plantingGuideView.setVisibility(View.VISIBLE);
                }

                /**
                 * Initialize the views in the weather widget layout
                 */
                private void initializeWeatherWidgetViews(View view) {
                    tvLocation = view.findViewById(R.id.tv_location);
                    btnLocation = view.findViewById(R.id.btn_location);
                    tvDate = view.findViewById(R.id.tv_date);
                    tvTemperature = view.findViewById(R.id.tv_temperature);
                    tvWeatherCondition = view.findViewById(R.id.tv_weather_condition);
                    ivWeatherIcon = view.findViewById(R.id.iv_weather_icon);
                    tvHumidity = view.findViewById(R.id.tv_humidity);
                    tvRainfall = view.findViewById(R.id.tv_rainfall);
                    tvWind = view.findViewById(R.id.tv_wind);
                    rvForecast = view.findViewById(R.id.rv_forecast);
                    rvPlantingRecommendations = view.findViewById(R.id.rv_planting_recommendations);
                    btnViewAllWeather = view.findViewById(R.id.btn_view_all_weather);

                    // Add progress bar to the layout if it doesn't exist
                    progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
                    progressBar.setVisibility(View.GONE);

                    // Set up the date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
                    String currentDate = dateFormat.format(new Date());
                    tvDate.setText(currentDate);

                    // Set up location button click listener
                    btnLocation.setOnClickListener(v -> requestLocationUpdates());

                    // Set up view all weather button click listener
                    btnViewAllWeather.setOnClickListener(v -> {
                        // Navigate to detailed weather view or expand the current view
                        Toast.makeText(getContext(), "View all weather data clicked", Toast.LENGTH_SHORT).show();
                    });

                    // Set up recycler views
                    rvForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    rvPlantingRecommendations.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                    // Initialize with mock data
                    updateWeatherWidgetUI();
                }

                /**
                 * Update the weather widget UI with current data
                 */
                private void updateWeatherWidgetUI() {
                    if (tvLocation == null) return; // Widget not initialized

                    // Update location
                    tvLocation.setText(locationText.getText());

                    // Update temperature and condition
                    tvTemperature.setText(currentTempText.getText());
                    tvWeatherCondition.setText(conditionText.getText());

                    // Update weather details
                    tvHumidity.setText(humidityText.getText().toString().replace("Humidity: ", ""));
                    tvRainfall.setText(rainText.getText().toString().replace("Rain: ", ""));
                    tvWind.setText(windText.getText().toString().replace("Wind: ", ""));

                    // Update forecast recycler view
                    ForecastAdapter forecastAdapter = new ForecastAdapter(getMockForecastData());
                    rvForecast.setAdapter(forecastAdapter);

                    // Update planting recommendations recycler view
                    PlantingRecommendationAdapter recommendationAdapter = new PlantingRecommendationAdapter(getMockRecommendationData());
                    rvPlantingRecommendations.setAdapter(recommendationAdapter);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set up forecast recycler view
        forecastRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        ForecastAdapter forecastAdapter = new ForecastAdapter(getMockForecastData());
        forecastRecyclerView.setAdapter(forecastAdapter);

        // Set up recommendations recycler view
        recommendationsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        PlantingRecommendationAdapter recommendationAdapter = new PlantingRecommendationAdapter(getMockRecommendationData());
        recommendationsRecyclerView.setAdapter(recommendationAdapter);

        // Set initial data
        locationText.setText("Banaue, Ifugao");
        currentTempText.setText("28°C");
        conditionText.setText("Partly Cloudy");
        humidityText.setText("Humidity: 75%");
        windText.setText("Wind: 8 km/h");
        rainText.setText("Rain: 30%");

        return view;
    }

    private List<ForecastDay> getMockForecastData() {
        List<ForecastDay> forecast = new ArrayList<>();
        forecast.add(new ForecastDay("Today", 29, 22, "Partly Cloudy", 30));
        forecast.add(new ForecastDay("Tomorrow", 30, 23, "Sunny", 10));
        forecast.add(new ForecastDay("Wed", 31, 24, "Sunny", 5));
        forecast.add(new ForecastDay("Thu", 29, 23, "Scattered Showers", 60));
        forecast.add(new ForecastDay("Fri", 27, 22, "Rain", 80));
        return forecast;
    }

    private List<PlantingRecommendation> getMockRecommendationData() {
        List<PlantingRecommendation> recommendations = new ArrayList<>();
        recommendations.add(new PlantingRecommendation(
                "Rice (Lowland)",
                "Favorable conditions for planting in the next 3 days",
                "Within this week",
                85));
        recommendations.add(new PlantingRecommendation(
                "Rice (Upland)",
                "Wait for more rainfall before planting",
                "In 1-2 weeks",
                60));
        recommendations.add(new PlantingRecommendation(
                "Vegetables",
                "Good conditions for leafy vegetables",
                "Now",
                90));
        return recommendations;
    }

    private void requestLocationUpdates() {
        if (getContext() == null) return;

        // Check if we already have permission
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request permission if we don't have it
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // We have permission, get location updates
            startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission") // We check permissions before calling this method
    private void startLocationUpdates() {
        if (getContext() == null) return;

        isLocationRequested = true;
        Toast.makeText(getContext(), getString(R.string.fetching_location), Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        // Try to get location from GPS first
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE,
                    this);
        }
        // If GPS is not available, try network provider
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE,
                    this);
        } else {
            // No location provider available
            Toast.makeText(getContext(), getString(R.string.location_error), Toast.LENGTH_SHORT).show();
        }

        // Try to get last known location immediately
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get location updates
                startLocationUpdates();
            } else {
                // Permission denied
                Toast.makeText(getContext(), getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (getContext() == null) return;

        // Stop requesting location updates after we get one
        if (locationManager != null && isLocationRequested) {
            locationManager.removeUpdates(this);
            isLocationRequested = false;
        }

        // In a real app, you would use reverse geocoding to get the city name
        // For this example, we'll just show the coordinates
        String locationString = String.format("%.4f, %.4f", location.getLatitude(), location.getLongitude());
        locationText.setText(locationString);

        // In a real app, you would call a weather API with these coordinates
        // For this example, we'll just update with mock data
        updateWeatherData(location);
    }

    private void updateWeatherData(Location location) {
        // In a real app, this would call a weather API
        // For this example, we'll just update with slightly different mock data

        // Simulate different weather based on location
        double latitude = location.getLatitude();
        int tempOffset = (int) (latitude % 5);

        currentTempText.setText((28 + tempOffset) + "°C");
        conditionText.setText(tempOffset > 2 ? "Sunny" : "Partly Cloudy");
        humidityText.setText("Humidity: " + (75 - tempOffset * 2) + "%");
        windText.setText("Wind: " + (8 + tempOffset) + " km/h");
        rainText.setText("Rain: " + (30 - tempOffset * 5) + "%");

        // Update forecast and recommendations
        ForecastAdapter forecastAdapter = new ForecastAdapter(getMockForecastData());
        forecastRecyclerView.setAdapter(forecastAdapter);

        PlantingRecommendationAdapter recommendationAdapter =
                new PlantingRecommendationAdapter(getMockRecommendationData());
        recommendationsRecyclerView.setAdapter(recommendationAdapter);

        // Update the weather widget UI if it's initialized
        updateWeatherWidgetUI();

        Toast.makeText(getContext(), "Weather updated for current location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Required for LocationListener interface but not used in newer Android versions
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // LocationListener interface method
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // LocationListener interface method
        if (getContext() != null) {
            Toast.makeText(getContext(), "Location provider disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove location updates when fragment is paused
        if (locationManager != null && isLocationRequested) {
            locationManager.removeUpdates(this);
            isLocationRequested = false;
        }
    }
}
