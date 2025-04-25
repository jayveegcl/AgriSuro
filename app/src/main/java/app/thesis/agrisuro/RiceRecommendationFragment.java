package app.thesis.agrisuro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.ForecastAdapter;
import app.thesis.agrisuro.RiceRecommendationAdapter;
import app.thesis.agrisuro.models.ForecastItem;
import app.thesis.agrisuro.RiceRecommendationItem;

public class RiceRecommendationFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private TextView temperatureText, conditionText, humidityText, windText, rainText, locationText;
    private ImageView weatherIcon;
    private FusedLocationProviderClient fusedLocationClient;

    private TextView textFeelsLike, textPressure, textVisibility, textUvIndex, lastUpdateText;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView forecastRecyclerView;
    private RecyclerView recommendationsRecyclerView;
    private FloatingActionButton locationFab;
    private ForecastAdapter forecastAdapter;
    private RiceRecommendationAdapter recommendationAdapter;
    private List<ForecastItem> forecastItems;
    private List<RiceRecommendationItem> recommendationItems;
    private TabLayout tabLayout;
    private LinearLayout currentWeatherView, plantingGuideView;
    private ImageButton refreshButton;

    // Weather data to use for recommendations
    private double currentTemp = 0;
    private double currentHumidity = 0;
    private double currentWindSpeed = 0;
    private double currentRainfall = 0;
    private String currentWeatherCondition = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Initialize UI components
        temperatureText = view.findViewById(R.id.textTemperature);
        conditionText = view.findViewById(R.id.textCondition);
        humidityText = view.findViewById(R.id.textHumidity);
        windText = view.findViewById(R.id.textWind);
        rainText = view.findViewById(R.id.textRain);
        locationText = view.findViewById(R.id.textLocation);
        weatherIcon = view.findViewById(R.id.weatherIcon);

        initializeOtherUiElements(view);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Set up tab listener for switching between current weather and planting guide
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    currentWeatherView.setVisibility(View.VISIBLE);
                    plantingGuideView.setVisibility(View.GONE);
                } else {
                    currentWeatherView.setVisibility(View.GONE);
                    plantingGuideView.setVisibility(View.VISIBLE);
                    generateRiceRecommendations();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set click listeners
        refreshButton.setOnClickListener(v -> checkLocationPermission());
        locationFab.setOnClickListener(v -> {
            checkLocationPermission();
            if (isAdded()) {
                Toast.makeText(requireContext(), "Updating location...", Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefresh.setOnRefreshListener(this::checkLocationPermission);

        // Initialize data and fetch weather
        checkLocationPermission();

        return view;
    }

    private void initializeOtherUiElements(View view) {
        textFeelsLike = view.findViewById(R.id.textFeelsLike);
        textPressure = view.findViewById(R.id.textPressure);
        textVisibility = view.findViewById(R.id.textVisibility);
        textUvIndex = view.findViewById(R.id.textUvIndex);
        lastUpdateText = view.findViewById(R.id.lastUpdateText);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        tabLayout = view.findViewById(R.id.weather_tabs);
        currentWeatherView = view.findViewById(R.id.current_weather_view);
        plantingGuideView = view.findViewById(R.id.planting_guide_view);
        refreshButton = view.findViewById(R.id.refresh_button);

        // Initialize forecast recycler view
        forecastRecyclerView = view.findViewById(R.id.forecastRecyclerView);
        forecastItems = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(forecastItems);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        forecastRecyclerView.setAdapter(forecastAdapter);

        // Initialize recommendations recycler view
        recommendationsRecyclerView = view.findViewById(R.id.recommendations_recycler_view);
        recommendationItems = new ArrayList<>();
        recommendationAdapter = new RiceRecommendationAdapter(recommendationItems);
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendationsRecyclerView.setAdapter(recommendationAdapter);

        locationFab = view.findViewById(R.id.locationFab);
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
        if (swipeRefresh != null) swipeRefresh.setRefreshing(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        getCityName(lat, lon);
                        getWeatherData(lat, lon);
                        getForecastData(lat, lon);
                    } else if (isAdded()) {
                        Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show();
                        if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                    }
                });
    }

    private void getCityName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (isAdded() && addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                locationText.setText(cityName != null ? cityName : "Unknown");
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (isAdded()) locationText.setText("Location: Error");
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

                currentTemp = main.getDouble("temp");
                String temperature = Math.round(currentTemp) + "°C";
                currentWeatherCondition = weather.getString("description");
                String condition = capitalizeFirstLetter(currentWeatherCondition);
                currentHumidity = main.getDouble("humidity");
                String humidity = currentHumidity + "%";
                currentWindSpeed = wind.getDouble("speed");
                String windSpeed = currentWindSpeed + " m/s";
                String icon = weather.getString("icon");
                String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                double feelsLike = main.getDouble("feels_like");
                int pressure = main.getInt("pressure");
                int visibility = jsonObject.has("visibility") ? jsonObject.getInt("visibility") / 1000 : 0;

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, MMM dd", Locale.getDefault());
                String currentTime = "Updated: " + sdf.format(new Date());

                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        temperatureText.setText(temperature);
                        conditionText.setText(condition);
                        humidityText.setText(humidity);
                        windText.setText(windSpeed);

                        textFeelsLike.setText("Feels like: " + Math.round(feelsLike) + "°C");
                        textPressure.setText(pressure + " hPa");
                        textVisibility.setText(visibility + " km");
                        textUvIndex.setText("N/A");
                        lastUpdateText.setText(currentTime);

                        try {
                            currentRainfall = 0;
                            String rainChance = "0 mm";
                            if (jsonObject.has("rain")) {
                                JSONObject rain = jsonObject.getJSONObject("rain");
                                if (rain.has("1h")) {
                                    currentRainfall = rain.getDouble("1h");
                                    rainChance = currentRainfall + " mm";
                                }
                            }
                            rainText.setText(rainChance);
                        } catch (Exception e) {
                            rainText.setText("0 mm");
                            currentRainfall = 0;
                        }

                        Glide.with(requireContext()).load(iconUrl).into(weatherIcon);

                        if (swipeRefresh != null) swipeRefresh.setRefreshing(false);

                        // Generate recommendations based on current weather
                        generateRiceRecommendations();
                    });
                }
            } catch (Exception e) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
                        if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                    });
                }
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

                forecastItems.clear();

                String currentDate = "";
                for (int i = 0; i < forecastList.length(); i++) {
                    JSONObject forecast = forecastList.getJSONObject(i);
                    String dateText = forecast.getString("dt_txt").split(" ")[0];

                    if (!dateText.equals(currentDate) && forecastItems.size() < 5) {
                        currentDate = dateText;

                        JSONObject main = forecast.getJSONObject("main");
                        JSONObject weather = forecast.getJSONArray("weather").getJSONObject(0);

                        double tempMax = main.getDouble("temp_max");
                        double tempMin = main.getDouble("temp_min");
                        String description = weather.getString("description");
                        String icon = weather.getString("icon");
                        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

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

                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        forecastAdapter.notifyDataSetChanged();
                        generateRiceRecommendations();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Error fetching forecast data", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }

    private void generateRiceRecommendations() {
        recommendationItems.clear();

        // Generate recommendation for current day based on current weather
        String currentRecommendation = generateCurrentDayRecommendation();
        recommendationItems.add(new RiceRecommendationItem("Ngayon", currentRecommendation));

        // Generate recommendations for forecasted days
        for (int i = 0; i < forecastItems.size(); i++) {
            ForecastItem forecastItem = forecastItems.get(i);
            String recommendation = generateForecastRecommendation(forecastItem, i + 1);
            recommendationItems.add(new RiceRecommendationItem(forecastItem.getDate(), recommendation));
        }

        recommendationAdapter.notifyDataSetChanged();
    }

    private String generateCurrentDayRecommendation() {
        StringBuilder recommendation = new StringBuilder();

        // Temperature-based recommendations
        if (currentTemp > 35) {
            recommendation.append("• Mag-ingat sa sobrang init! Siguruhing may sapat na tubig ang iyong palayan. ");
            recommendation.append("Magpatubig nang mas malalim para maprotektahan ang mga ugat sa init.\n\n");
        } else if (currentTemp < 20) {
            recommendation.append("• Mag-ingat sa malamig na temperatura. Posibleng mabagal ang paglaki ng bigas. ");
            recommendation.append("Iwasang magpatubig ng sobra dahil mas malamig ang tubig kaysa hangin.\n\n");
        } else {
            recommendation.append("• Magandang temperatura para sa paglaki ng palay. ");
            recommendation.append("Panatilihin ang tamang lebel ng tubig sa palayan.\n\n");
        }

        // Rainfall-based recommendations
        if (currentRainfall > 25) {
            recommendation.append("• Malakas ang pag-ulan! Suriin ang mga kanal para maiwasan ang pagbaha. ");
            recommendation.append("Siguraduhing may maayos na daluyan ng tubig palabas ng bukid.\n\n");
        } else if (currentRainfall > 10) {
            recommendation.append("• May katamtamang ulan. Magandang pagkakataon para sa natural na pagpatubig. ");
            recommendation.append("Hindi na kailangang magpatubig muna.\n\n");
        } else if (currentRainfall > 0) {
            recommendation.append("• May bahagyang ulan. Suriin ang kakulangan ng tubig sa palayan.\n\n");
        } else {
            recommendation.append("• Walang ulan. Kailangang magpatubig ng sapat sa palayan.\n\n");
        }

        // Wind-based recommendations
        if (currentWindSpeed > 10) {
            recommendation.append("• Malakas ang hangin! Mag-ingat sa posibleng pagkasira ng mga tanim. ");
            recommendation.append("Kung kaya, maglagay ng harang-hangin o windbreak.\n\n");
        } else if (currentWindSpeed > 5) {
            recommendation.append("• Katamtamang hangin. Maganda ito para sa bentilasyon ngunit suriin ");
            recommendation.append("ang mga halaman kung may mga natumba.\n\n");
        }

        // Humidity-based recommendations
        if (currentHumidity > 85) {
            recommendation.append("• Mataas ang halumigmig. May panganib ng fungal diseases. ");
            recommendation.append("Suriin ang mga dahon para sa sakit at iwasang sobrang patubig.\n\n");
        } else if (currentHumidity < 60) {
            recommendation.append("• Mababa ang halumigmig. Dagdagan ang pagpatubig para maiwasan ang pagkatuyo ng lupa.\n\n");
        }

        // Weather condition specific recommendations
        if (currentWeatherCondition.contains("rain") || currentWeatherCondition.contains("drizzle")) {
            recommendation.append("• May ulan. Magandang pagkakataon para mag-apply ng fertilizer ");
            recommendation.append("kung kinakailangan dahil makakatulong ang tubig-ulan para ito'y tumalab.\n\n");
        } else if (currentWeatherCondition.contains("thunder") || currentWeatherCondition.contains("storm")) {
            recommendation.append("• May kidlat at kulog! Iwasang magtrabaho sa bukid sa panahong ito para sa kaligtasan.\n\n");
        } else if (currentWeatherCondition.contains("clear")) {
            recommendation.append("• Magandang panahon para sa pagpapatuyo ng mga ani kung panahon ng anihan.\n\n");
            recommendation.append("• Magandang pagkakataon din para mag-spray ng pestisidyo kung kinakailangan.\n\n");
        } else if (currentWeatherCondition.contains("cloud")) {
            recommendation.append("• Maulap ngunit magandang panahon pa rin para sa mga gawain sa bukid. ");
            recommendation.append("Pagandahin ang pagkakataon na hindi masyadong mainit.\n\n");
        }

        return recommendation.toString();
    }

    private String generateForecastRecommendation(ForecastItem forecast, int daysAhead) {
        StringBuilder recommendation = new StringBuilder();

        // Convert string temperature to double for comparison
        String maxTempStr = forecast.getMaxTemp().replace("°", "");
        String minTempStr = forecast.getMinTemp().replace("°", "");
        double maxTemp = Double.parseDouble(maxTempStr);
        double minTemp = Double.parseDouble(minTempStr);
        String condition = forecast.getDescription().toLowerCase();

        // Basic recommendations based on day in advance
        recommendation.append("• ");
        switch (daysAhead) {
            case 1:
                recommendation.append("Bukas: ");
                break;
            case 2:
                recommendation.append("Sa makalawa: ");
                break;
            default:
                recommendation.append("Sa darating na araw: ");
                break;
        }

        // Temperature based recommendations
        if (maxTemp > 35) {
            recommendation.append("Inaasahang sobrang init. Siguruhing sapat ang tubig sa palayan. ");
            recommendation.append("Iwasang mag-spray ng pestisidyo sa pinakamainit na oras ng araw.\n\n");
        } else if (minTemp < 20) {
            recommendation.append("Posibleng malamig sa umaga. Maaaring mas mabagal ang paglaki ng palay. ");
            recommendation.append("Iwasang magpatubig ng malalim sa gabi.\n\n");
        } else {
            recommendation.append("Magandang temperatura para sa paglaki ng palay. ");
            recommendation.append("Tamang panahon para sa mga regular na gawain sa palayan.\n\n");
        }

        // Weather condition based recommendations
        if (condition.contains("rain") || condition.contains("drizzle")) {
            recommendation.append("• May inaasahang pag-ulan. Maghanda ng daluyan ng tubig. ");
            recommendation.append("Magandang pagkakataon para maglagay ng abono kung kinakailangan.\n\n");
        } else if (condition.contains("storm") || condition.contains("thunder")) {
            recommendation.append("• May bagyong paparating! Siguruhing may maayos na daluyan ang tubig ");
            recommendation.append("para maiwasan ang pagbaha. Iwasang mag-spray ng anumang kemikal.\n\n");
        } else if (condition.contains("clear")) {
            recommendation.append("• Inaasahang maaraw. Magandang pagkakataon para sa pagpapataba o pag-spray. ");
            recommendation.append("Siguraduhing sapat ang tubig sa palayan lalo na kung mataas ang temperatura.\n\n");
        } else if (condition.contains("cloud")) {
            recommendation.append("• Maulap na panahon. Magandang panahon para sa mga gawain sa bukid ");
            recommendation.append("dahil hindi masyadong mainit.\n\n");
        }

        // Add seasonal recommendations based on the current month
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        int currentMonth = Integer.parseInt(monthFormat.format(new Date()));

        if (currentMonth >= 6 && currentMonth <= 10) { // Wet season in Philippines
            recommendation.append("• Panahon ng tag-ulan: Mag-ingat sa mga peste at sakit na karaniwang ");
            recommendation.append("lumalabas sa panahong maalinsangan at maulan.\n\n");
        } else { // Dry season
            recommendation.append("• Panahon ng tag-init: Siguraduhing regular ang pagpatubig dahil mabilis ");
            recommendation.append("matuyo ang lupa sa panahong ito.\n\n");
        }

        return recommendation.toString();
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
            } else if (isAdded()) {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            }
        }
    }
}