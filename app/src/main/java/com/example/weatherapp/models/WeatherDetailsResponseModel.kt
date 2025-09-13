package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherDetailsResponseModel(

    @SerializedName("id")
    @Expose
    public val id: Int,

    @SerializedName("main")
    @Expose
    public val main: String,

    @SerializedName("description")
    @Expose
    public val description: String,

    @SerializedName("icon")
    @Expose
    public val icon: String
) {
    companion object {
        private val TAG = WeatherDetailsResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(id=$id, main=$main, description=$description, icon=$icon)"
    }
}