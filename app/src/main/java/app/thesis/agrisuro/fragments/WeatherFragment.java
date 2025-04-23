package app.thesis.agrisuro.fragments;

import android.Manifest;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import app.thesis.agrisuro.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private TextView temperatureText, conditionText, humidityText, windText, rainText, locationText;
    private ImageView weatherIcon;
    private FusedLocationProviderClient fusedLocationClient;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        checkLocationPermission();

        return view;
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
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            getCityName(lat, lon);  // Get city name from lat/lon
                            getWeatherData(lat, lon);
                        } else {
                            Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show();
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

                String temperature = main.getString("temp") + "°C";
                String condition = weather.getString("description");
                String humidity = "Humidity: " + main.getString("humidity") + "%";
                String windSpeed = "Wind: " + wind.getString("speed") + " m/s";
                String icon = weather.getString("icon");
                String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                requireActivity().runOnUiThread(() -> {
                    temperatureText.setText(temperature);
                    conditionText.setText(condition);
                    humidityText.setText(humidity);
                    windText.setText(windSpeed);

                    try {
                        String rainChance = "Rain: N/A";
                        if (jsonObject.has("rain")) {
                            JSONObject rain = jsonObject.getJSONObject("rain");
                            if (rain.has("1h")) {
                                rainChance = "Rain (1h): " + rain.getDouble("1h") + " mm";
                            }
                        }
                        rainText.setText(rainChance);
                    } catch (Exception e) {
                        rainText.setText("Rain: error");
                    }

                    Glide.with(requireContext()).load(iconUrl).into(weatherIcon);
                });

            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
