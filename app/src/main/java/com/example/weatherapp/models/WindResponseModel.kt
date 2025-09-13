package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WindResponseModel(

    @SerializedName("speed")
    @Expose
    public val speed: Double,

    @SerializedName("deg")
    @Expose
    public val deg: Int,

    @SerializedName("gust")
    @Expose
    public val gust: Double
) {
    companion object {
        private val TAG = WindResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(speed=$speed, deg=$deg, gust=$gust)"
    }
}