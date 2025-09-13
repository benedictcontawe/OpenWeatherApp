package com.example.weatherapp.models

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Immutable
public data class WeatherRequestModel(
    @SerializedName("appid")
    @Expose
    public val key: String,

    @SerializedName("lat")
    @Expose
    public val latitude: Double,

    @SerializedName("lon")
    @Expose
    public val longitude: Double
) {
    companion object {
        private val TAG = WeatherRequestModel::class.java.getSimpleName()
    }

    public fun toMap(): Map<String, String> {
        return mapOf(
            "appid" to this.key,
            "lat" to this.latitude.toString(),
            "lon" to this.longitude.toString()
        )
    }

    override fun toString() : String {
        return "$TAG(key=$key, latitude=$latitude, longitude=$longitude)" ?: super.toString()
    }
}