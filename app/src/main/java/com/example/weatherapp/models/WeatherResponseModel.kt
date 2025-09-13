package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherResponseModel(

    @SerializedName("coord")
    @Expose
    public val coord: CoordResponseModel,

    @SerializedName("weather")
    @Expose
    public val weather: List<WeatherDetailsResponseModel>,

    @SerializedName("base")
    @Expose
    public val base: String,

    @SerializedName("main")
    @Expose
    public val main: MainResponseModel,

    @SerializedName("visibility")
    @Expose
    public val visibility: Int,

    @SerializedName("wind")
    @Expose
    public val wind: WindResponseModel,

    @SerializedName("clouds")
    @Expose
    public val clouds: CloudsResponseModel,

    @SerializedName("dt")
    @Expose
    public val dt: Long,

    @SerializedName("sys")
    @Expose
    public val sys: SysResponseModel,

    @SerializedName("timezone")
    @Expose
    public val timezone: Int,

    @SerializedName("id")
    @Expose
    public val id: Int,

    @SerializedName("name")
    @Expose
    public val name: String,

    @SerializedName("cod")
    @Expose
    public val cod: Int
) {
    companion object {
        private val TAG = WeatherResponseModel::class.java.getSimpleName()
    }

    override fun toString(): String {
        return "$TAG($coord, $weather, $base, $main, $visibility, $wind, $clouds, $dt, $sys $timezone, $id, $name, $cod)" ?: super.toString()
    }
}
