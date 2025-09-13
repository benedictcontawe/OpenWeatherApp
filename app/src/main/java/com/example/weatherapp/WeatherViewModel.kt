package com.example.weatherapp

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.activities.MainActivity
import com.example.weatherapp.models.WeatherRequestModel
import com.example.weatherapp.models.WeatherResponseModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : AndroidViewModel {

    companion object {
        private val TAG : String = WeatherViewModel::class.java.getSimpleName()
    }

    private val repository : Repository
    private val fusedLocationClient : FusedLocationProviderClient
    private val liveResponse : MutableLiveData<WeatherResponseModel?>
    private val isRefreshing : MutableStateFlow<Boolean>

    constructor(application : Application) : super(application) {
        Log.d(TAG, "constructor")
        repository = Repository()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application ?: getApplication<Application>())
        isRefreshing = MutableStateFlow<Boolean>(false)
        liveResponse = MutableLiveData<WeatherResponseModel?>(null)
    }

    public fun onCheckLocationPermission(activity : MainActivity) {
        ManifestPermission.checkSelfPermission(
            getApplication<Application>(),
            ManifestPermission.locationPermission,
            isGranted = {
                requestWeather()
            }, isDenied = {
                ManifestPermission.requestPermissions(activity,
                    ManifestPermission.locationPermission,
                    ManifestPermission.LOCATION_PERMISSION_CODE
                )
            }
        )
    }

    public fun requestWeather() {
        //isRefreshing.emit(true)
        //isRefreshing.value = true
        var location : Location?
        var request : WeatherRequestModel
        var response : WeatherResponseModel?
        ManifestPermission.checkSelfPermission (
            getApplication<Application>(),
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            isGranted = {
                fusedLocationClient.getLastLocation().addOnSuccessListener { location ->
                        viewModelScope.launch(Dispatchers.IO) {
                            request = WeatherRequestModel(
                                Constants.API_KEY,
                                location?.getLatitude()!!,
                                location?.getLongitude()!!
                            )
                            response = repository.getWeather(request)
                            Log.d(TAG, "requestWeather() ${response.toString()}")
                            liveResponse.postValue(response)//TODO: Handle response
                            isRefreshing.emit(false)
                    }
                }
            }, isDenied = {
                viewModelScope.launch(Dispatchers.IO) {
                    request = WeatherRequestModel(Constants.API_KEY, 44.34, 10.99)
                    response = repository.getWeather(request)
                    Log.d(TAG, "requestWeather() ${response.toString()}")
                    liveResponse.postValue(response)//TODO: Handle response
                    isRefreshing.emit(false)
                }
            }
        )
    }

    public fun observeRefreshing() : StateFlow<Boolean> {
        return isRefreshing.asStateFlow<Boolean>()
    }

    public fun observeWeather() : LiveData<WeatherResponseModel?> = liveResponse
}