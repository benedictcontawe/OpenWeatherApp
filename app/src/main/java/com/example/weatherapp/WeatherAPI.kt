package com.example.weatherapp

import com.example.weatherapp.models.WeatherResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

//https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
public interface WeatherAPI {
    @GET(Constants.API_GET)
    public fun getWeather(
        @QueryMap parameters: Map<String, String>
    ) : Call<WeatherResponseModel>
}