package com.example.weatherapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.activities.MainActivity
import com.example.weatherapp.models.ForecastResponseModel
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.models.WeatherRequestModel
import com.example.weatherapp.models.WeatherResponseModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WeatherViewModel : AndroidViewModel {

    companion object {
        private val TAG : String = WeatherViewModel::class.java.getSimpleName()
    }

    private val repository : Repository
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val fusedLocationClient : FusedLocationProviderClient
    private val liveWeather : MutableLiveData<WeatherModel?>
    private val liveForcastResponse : MutableLiveData<ForecastResponseModel?>
    private val isRefreshing : MutableStateFlow<Boolean>

    constructor(application : Application) : super(application) {
        Log.d(TAG, "constructor")
        repository = Repository()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application ?: getApplication<Application>())
        isRefreshing = MutableStateFlow<Boolean>(false)
        liveWeather = MutableLiveData<WeatherModel?>(null)
        liveForcastResponse = MutableLiveData<ForecastResponseModel?>(null)
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
                            Log.d(TAG, "requestWeather() ${request.toString()}")
                            response = repository.getWeather(request)
                            val model : WeatherModel = WeatherModel(
                                response!!.id.toString(),
                                response!!.sys.country,
                                response!!.weather.first().main,
                                response!!.main.temp.toString(),
                                unixToTime(response!!.sys.sunrise),
                                unixToTime(response!!.sys.sunset),
                            )
                            Log.d(TAG, "requestWeather() ${response.toString()}")
                            liveWeather.postValue(model)
                            isRefreshing.emit(false)
                    }
                }
            }, isDenied = {
                viewModelScope.launch(Dispatchers.IO) {
                    request = WeatherRequestModel(Constants.API_KEY, 44.34, 10.99)
                    response = repository.getWeather(request)
                    val model : WeatherModel = WeatherModel(
                        response!!.id.toString(),
                        response!!.sys.country,
                        response!!.weather.first().main,
                        response!!.main.temp.toString(),
                        unixToTime(response!!.sys.sunrise),
                        unixToTime(response!!.sys.sunset),
                    )
                    Log.d(TAG, "requestWeather() ${response.toString()}")
                    liveWeather.postValue(model)
                    isRefreshing.emit(false)
                }
            }
        )
    }

    public fun requestForecast() {
        var request : WeatherRequestModel
        var response : ForecastResponseModel?
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
                        response = repository.getForecast(request)
                        Log.d(TAG, "requestWeather() ${response.toString()}")
                        liveForcastResponse.postValue(response)//TODO: Handle response
                        isRefreshing.emit(false)
                    }
                }
            }, isDenied = {
                viewModelScope.launch(Dispatchers.IO) {
                    request = WeatherRequestModel(Constants.API_KEY, 44.34, 10.99)
                    response = repository.getForecast(request)
                    Log.d(TAG, "requestWeather() ${response.toString()}")
                    liveForcastResponse.postValue(response)//TODO: Handle response
                    isRefreshing.emit(false)
                }
            }
        )
    }

    private fun unixToTime(unix: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(unix * 1000))
    }

    public fun observeRefreshing() : StateFlow<Boolean> {
        return isRefreshing.asStateFlow<Boolean>()
    }

    public fun observeWeather() : LiveData<WeatherModel?> = liveWeather

    public fun observeForcast() : LiveData<ForecastResponseModel?> = liveForcastResponse

    fun getUser() : FirebaseUser? {
        return firebaseAuth.getCurrentUser()
    }

    fun isUserSignedIn() : Boolean {
        return if (getUser() != null) true else false
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}