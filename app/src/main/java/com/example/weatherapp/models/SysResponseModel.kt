package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SysResponseModel(

    @SerializedName("type")
    @Expose
    public val type: Int,

    @SerializedName("id")
    @Expose
    public val id: Int,

    @SerializedName("country")
    @Expose
    public val country: String,

    @SerializedName("sunrise")
    @Expose
    public val sunrise: Long,

    @SerializedName("sunset")
    @Expose
    public val sunset: Long
) {
    companion object {
        private val TAG = SysResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(type=$type, id=$id, country=$country, sunrise=$sunrise, sunset=$sunset)"
    }
}
