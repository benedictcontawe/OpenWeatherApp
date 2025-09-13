package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastResponseModel(
    @SerializedName("cod")
    @Expose
    val cod: String,

    @SerializedName("message")
    @Expose
    val message: Int,

    @SerializedName("cnt")
    @Expose
    val cnt: Int,

    @SerializedName("list")
    @Expose
    val list: List<ForecastDetailsResponseModel>,

    @SerializedName("city")
    @Expose
    val city: CityResponseModel
) {
    companion object {
        private val TAG = ForecastResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(cod=$cod, message=$message, cnt=$cnt, listCount=${list.size}, city=$city)"
    }
}
