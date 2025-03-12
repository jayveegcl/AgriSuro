package com.example.agrisuro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<WeatherResponse> getWeather(
        @Query("lat") double latitude,
        @Query("lon") double longitude,
        @Query("appid") String apiKey,
        @Query("units") String units
    );
}
