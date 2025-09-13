package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastDetailsResponseModel(
    @SerializedName("dt")
    @Expose
    val dt: Long,

    @SerializedName("main")
    @Expose
    val main: MainResponseModel,

    @SerializedName("weather")
    @Expose
    val weather: List<WeatherDetailsResponseModel>,

    @SerializedName("clouds")
    @Expose
    val clouds: CloudsResponseModel,

    @SerializedName("wind")
    @Expose
    val wind: WindResponseModel,

    @SerializedName("visibility")
    @Expose
    val visibility: Int,

    @SerializedName("pop")
    @Expose
    val pop: Double, // Probability of precipitation

    @SerializedName("sys")
    @Expose
    val sys: SysResponseModel,

    @SerializedName("dt_txt")
    @Expose
    val dtTxt: String // Time of data forecasted in text format
) {
    companion object {
        private val TAG = ForecastDetailsResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(dt=$dt, main=$main, weather=$weather, clouds=$clouds, wind=$wind, visibility=$visibility, pop=$pop, sys=$sys, dtTxt=$dtTxt)"
    }
}
