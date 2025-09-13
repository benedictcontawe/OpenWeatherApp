package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MainResponseModel(

    @SerializedName("temp")
    @Expose
    public val temp: Double,

    @SerializedName("feels_like")
    @Expose
    public val feelsLike: Double,

    @SerializedName("temp_min")
    @Expose
    public val tempMin: Double,

    @SerializedName("temp_max")
    @Expose
    public val tempMax: Double,

    @SerializedName("pressure")
    @Expose
    public val pressure: Int,

    @SerializedName("humidity")
    @Expose
    public val humidity: Int,

    @SerializedName("sea_level")
    @Expose
    public val seaLevel: Int,

    @SerializedName("grnd_level")
    @Expose
    public val grndLevel: Int
) {
    companion object {
        private val TAG = MainResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(temp=$temp, feelsLike=$feelsLike, tempMin=$tempMin, tempMax=$tempMax, pressure=$pressure, humidity=$humidity, seaLevel=$seaLevel, grndLevel=$grndLevel)"
    }
}