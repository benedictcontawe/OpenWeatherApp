package com.example.weatherapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.WeatherViewModel
import com.example.weatherapp.models.ForecastModel
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import java.util.Calendar
import java.util.Locale

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
                    val weatherModel : WeatherModel? by viewModel.observeWeather().observeAsState(null)
                    val forecastList : List<ForecastModel>? by viewModel.observeForecast().observeAsState(null)
                    Box(
                        modifier = Modifier
                            .fillMaxSize().padding(innerPadding)
                    ) {
                        weatherModel?.let { weather ->
                            Image(
                                painter = painterResource(id = R.drawable.backgound_image),
                                contentDescription = "Background Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f))
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp, vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Wednesday, July 22",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${weather.city}, ${weather.country}",
                                        color = Color.White,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val icon = when {
                                        weather.condition.contains("rain", true) -> R.drawable.ic_rain
                                        else -> R.drawable.ic_sun
                                    }
                                    Image(
                                        painter = painterResource(icon),
                                        contentDescription = "Weather",
                                        modifier = Modifier.size(120.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = weather.condition,
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${weather.temperature}°C",
                                        color = Color.White,
                                        fontSize = 64.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            painter = painterResource(R.drawable.weather_sunset_up),
                                            contentDescription = "Sunrise",
                                            tint = Color.Yellow.copy(alpha = 0.8f),
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = weather.sunriseTime,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(25.dp))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            painter = painterResource(R.drawable.weather_sunset),
                                            contentDescription = "Sunset",
                                            tint = Color.Yellow.copy(alpha = 0.8f),
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = weather.sunsetTime,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                Column (
                                    horizontalAlignment = Alignment.Start,
                                ){
                                    Text(
                                        text = "Forecasting:",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    // GlassCard overlay with 5-day forecast
                                    GlassCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp),
                                        blurRadius = 32.dp
                                    ) {
                                        val forecastDays = 5
                                        val calendar = Calendar.getInstance()
                                        val dayNames = mutableListOf<String>()
                                        // Get the next 5 days
                                        for (i in 0 until forecastDays) {
                                            val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
                                            dayNames.add(dayOfWeek ?: "")
                                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                                        }
                                        // Sample temperatures, replace with your actual forecast data if available
                                        val temps = listOf(29, 31, 28, 27, 25)
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp)
                                        ) {
                                            items(dayNames.zip(temps)) { (day, temp) ->
                                                Card(
                                                    modifier = Modifier
                                                        .width(100.dp)
                                                        .height(140.dp)
                                                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                                                    shape = RoundedCornerShape(16.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color.White.copy(alpha = 0.1f)
                                                    )
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(12.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Text(
                                                            text = day,
                                                            color = Color.White.copy(alpha = 0.85f),
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        Icon(
                                                            painter = painterResource(R.drawable.ic_sun),
                                                            contentDescription = null,
                                                            tint = Color.Yellow.copy(alpha = 0.8f),
                                                            modifier = Modifier.size(32.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        Text(
                                                            text = "${temp}°",
                                                            color = Color.White,
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        } ?: CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun GlassCard(
        modifier: Modifier = Modifier,
        blurRadius: Dp = 16.dp,
        backgroundColor: Color = Color.White.copy(alpha = 0.15f),
        cornerRadius: Dp = 16.dp,
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color.Transparent),
        ) {
            Box (
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(backgroundColor)
                    .blur(radius = blurRadius)
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestWeather()
        viewModel.requestForecast()
    }
}