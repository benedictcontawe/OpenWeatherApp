package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoordResponseModel(

    @SerializedName("lon")
    @Expose
    public val lon: Double,

    @SerializedName("lat")
    @Expose
    public val lat: Double
) {
    companion object {
        private val TAG = CoordResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(lon=$lon, lat=$lat)"
    }
}