package com.example.weatherapp

import com.example.weatherapp.models.ForecastResponseModel
import com.example.weatherapp.models.WeatherResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

public interface WeatherAPI {
    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    @GET(Constants.API_GET_WEATHER)
    public fun getWeather(
        @QueryMap parameters: Map<String, String>
    ) : Call<WeatherResponseModel>

    @GET(Constants.API_GET_WEATHER)
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric" // Celsius
    ): Any
    //api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
    @GET(Constants.API_GET_FORECAST)
    public fun getForecast(
        @QueryMap parameters: Map<String, String>
    ) : Call<ForecastResponseModel>

    @GET(Constants.API_GET_FORECAST)
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Any
}