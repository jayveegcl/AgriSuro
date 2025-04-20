package com.agrisuro.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrisuro.app.adapters.ForecastAdapter;
import com.agrisuro.app.adapters.PlantingRecommendationAdapter;
import com.agrisuro.app.models.ForecastDay;
import com.agrisuro.app.models.PlantingRecommendation;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Initialize UI components
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

        // Set up location button
        locationButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Location selector would open here", Toast.LENGTH_SHORT).show();
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
}
