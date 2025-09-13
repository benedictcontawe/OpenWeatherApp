package com.example.weatherapp.models

data class WeatherModel (
    public val city : String,
    public val country : String,
    public val condition : String,
    public val temperature : String,
    public val sunriseTime : String,
    public val sunsetTime : String,
) {
    companion object {
        private val TAG = WeatherDetailsResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(city=$city, country=$country, condition=$condition, temperature=$temperature, sunriseTime=$sunriseTime, sunsetTime=$sunsetTime)"
    }
}