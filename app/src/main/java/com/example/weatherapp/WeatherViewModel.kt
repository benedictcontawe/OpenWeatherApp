package com.example.weatherapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.activities.MainActivity
import com.example.weatherapp.models.ForecastModel
import com.example.weatherapp.models.ForecastResponseModel
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.models.WeatherRequestModel
import com.example.weatherapp.models.WeatherResponseModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
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
    private val liveForcast : MutableLiveData<List<ForecastModel>?>

    constructor(application : Application) : super(application) {
        Log.d(TAG, "constructor")
        repository = Repository()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application ?: getApplication<Application>())
        liveWeather = MutableLiveData<WeatherModel?>(null)
        liveForcast = MutableLiveData<List<ForecastModel>?>(listOf<ForecastModel>())
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
                                response!!.name,
                                response!!.sys.country,
                                response!!.weather.first().main,
                                convertToCelciusFromKelvin(response!!.main.temp),
                                unixToTime(response!!.sys.sunrise),
                                unixToTime(response!!.sys.sunset),
                            )
                            Log.d(TAG, "requestWeather() ${response.toString()}")
                            liveWeather.postValue(model)
                    }
                }
            }, isDenied = {
                viewModelScope.launch(Dispatchers.IO) {
                    request = WeatherRequestModel(Constants.API_KEY, 44.34, 10.99)
                    Log.w(TAG, "requestWeather() ${request.toString()}")
                    response = repository.getWeather(request)
                    val model : WeatherModel = WeatherModel(
                        response!!.name,
                        response!!.sys.country,
                        response!!.weather.first().main,
                        convertToCelciusFromKelvin(response!!.main.temp),
                        unixToTime(response!!.sys.sunrise),
                        unixToTime(response!!.sys.sunset),
                    )
                    Log.w(TAG, "requestWeather() ${response.toString()}")
                    liveWeather.postValue(model)
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
                        Log.d(TAG, "requestForecast() ${request.toString()}")
                        response = repository.getForecast(request)
                        val list : MutableList<ForecastModel> = mutableListOf<ForecastModel>()
                        response?.list?.forEach {
                            list.add(
                                ForecastModel(
                                    it.dtTxt,
                                    it.weather.first().main,
                                    convertToCelciusFromKelvin(it.main.temp)
                                )
                            )
                        }
                        Log.d(TAG, "requestForecast() ${list.joinToString(", ")}")
                        liveForcast.postValue(list)
                    }
                }
            }, isDenied = {
                viewModelScope.launch(Dispatchers.IO) {
                    request = WeatherRequestModel(Constants.API_KEY, 44.34, 10.99)
                    Log.d(TAG, "requestForecast() ${request.toString()}")
                    response = repository.getForecast(request)
                    Log.d(TAG, "requestForecast() ${response.toString()}")
                    val list : MutableList<ForecastModel> = mutableListOf<ForecastModel>()
                    liveForcast.postValue(list)
                }
            }
        )
    }

    private fun unixToTime(unix: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(unix * 1000))
    }

    fun convertToCelcius(fahrenheit : Double) : String {
        val celsius = (fahrenheit - 32) * 5 / 9
        return String.format("%.2f", celsius)
    }

    fun convertToCelciusFromKelvin(kelvin: Double): String {
        val celsius = kelvin - 273.15
        return String.format("%.2f", celsius)
    }

    public fun observeWeather() : LiveData<WeatherModel?> = liveWeather

    public fun observeForecast() : LiveData<List<ForecastModel>?> = liveForcast

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