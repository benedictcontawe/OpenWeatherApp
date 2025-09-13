package com.example.weatherapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CloudsResponseModel(

    @SerializedName("all")
    @Expose
    public val all: Int
) {
    companion object {
        private val TAG = CloudsResponseModel::class.java.simpleName
    }

    override fun toString(): String {
        return "$TAG(all=$all)"
    }
}