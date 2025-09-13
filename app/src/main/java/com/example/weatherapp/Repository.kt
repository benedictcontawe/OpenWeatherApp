package com.example.weatherapp

import android.util.Log
import com.example.weatherapp.models.ForecastResponseModel
import com.example.weatherapp.models.WeatherRequestModel
import com.example.weatherapp.models.WeatherResponseModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

public class Repository {

    companion object {
        private val TAG : String = Repository::class.java.getSimpleName()
        private var retrofit : Retrofit? = null
    }

    private val weatherAPI : WeatherAPI

    constructor() {
        if (retrofit == null) retrofit = provideRetrofit(
            Constants.API_DOMAIN,
            provideGsonBuilder(),
            provideOkHttpClient()
        )
        weatherAPI = createService(WeatherAPI::class.java)
    }

    private fun provideRetrofit(url : String, gson : Gson, okHttpClient : OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    private fun provideGsonBuilder() : Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    private fun provideOkHttpClient() : OkHttpClient {
        return  OkHttpClient.Builder()
            .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    public suspend fun getWeather(request : WeatherRequestModel) : WeatherResponseModel? {
        Log.d(TAG,"getWeather() ${request.toString()}")
        val response : Response<WeatherResponseModel> = weatherAPI.getWeather(request.toMap()).execute()
        Log.d(TAG,"isSuccessful() ${response.isSuccessful()}")
        Log.d(TAG,"errorBody() ${response.errorBody()}")
        Log.d(TAG,"body() ${response.body()}")
        Log.d(TAG,"code() ${response.code()}")
        Log.d(TAG,"headers() ${response.headers()}")
        Log.d(TAG,"message() ${response.message()}")
        Log.d(TAG,"raw() ${response.raw()}")
        return if (response.isSuccessful() && response.body() != null) response.body()!!
        else if (response.isSuccessful().not()) null
        else null
    }

    public suspend fun getForecast(request : WeatherRequestModel) : ForecastResponseModel? {
        Log.d(TAG,"getWeather() ${request.toString()}")
        val response : Response<ForecastResponseModel> = weatherAPI.getForecast(request.toMap()).execute()
        Log.d(TAG,"isSuccessful() ${response.isSuccessful()}")
        Log.d(TAG,"errorBody() ${response.errorBody()}")
        Log.d(TAG,"body() ${response.body()}")
        Log.d(TAG,"code() ${response.code()}")
        Log.d(TAG,"headers() ${response.headers()}")
        Log.d(TAG,"message() ${response.message()}")
        Log.d(TAG,"raw() ${response.raw()}")
        return if (response.isSuccessful() && response.body() != null) response.body()!!
        else if (response.isSuccessful().not()) null
        else null
    }

    private fun <S> createService(serviceClass : Class<S>?) : S {
        return retrofit!!.create(serviceClass)
    }
}