package com.example.weatherapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.weatherapp.WeatherViewModel
import com.example.weatherapp.models.WeatherResponseModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

public class MainActivity : ComponentActivity() {

    companion object {
        private val TAG = MainActivity::class.java.getSimpleName()
    }

    private val viewModel : WeatherViewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.onCheckLocationPermission(this@MainActivity)
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestWeather()
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        val response : WeatherResponseModel? by viewModel.observeWeather().observeAsState(null)
        Text(
            text = "Hello ${response.toString()}",
            modifier = modifier
        )
    }
}