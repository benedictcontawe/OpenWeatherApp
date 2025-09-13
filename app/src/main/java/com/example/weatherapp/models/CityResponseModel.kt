package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityResponseModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("coord")
    @Expose
    val coord: CoordResponseModel,

    @SerializedName("country")
    @Expose
    val country: String,

    @SerializedName("population")
    @Expose
    val population: Int,

    @SerializedName("timezone")
    @Expose
    val timezone: Int,

    @SerializedName("sunrise")
    @Expose
    val sunrise: Long,

    @SerializedName("sunset")
    @Expose
    val sunset: Long
) {
    companion object {
        private val TAG = CityResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(id=$id, name=$name, coord=$coord, country=$country, population=$population, timezone=$timezone, sunrise=$sunrise, sunset=$sunset)"
    }
}
