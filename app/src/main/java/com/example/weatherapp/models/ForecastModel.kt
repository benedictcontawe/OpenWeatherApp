package com.example.weatherapp.models

data class ForecastModel (
    public val time : String,
    public val condition : String,
    public val temperature : String,
    //public val sunriseTime : String,
    //public val sunsetTime : String,
) {
    companion object {
        private val TAG = ForecastModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(time=$time, condition=$condition, temperature=$temperature)"
    }
}