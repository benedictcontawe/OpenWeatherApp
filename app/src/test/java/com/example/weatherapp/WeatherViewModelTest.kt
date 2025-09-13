package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherapp.models.MainResponseModel
import com.example.weatherapp.models.SysResponseModel
import com.example.weatherapp.models.WeatherDetailsResponseModel
import com.example.weatherapp.models.WeatherModel
import com.example.weatherapp.models.WeatherResponseModel
import com.example.weatherapp.utils.convertToCelciusFromKelvin
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockApplication: Application

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockRepository: Repository

    @Mock
    private lateinit var mockFusedLocationClient: FusedLocationProviderClient

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        mockkStatic("com.example.weatherapp.utils.ManifestPermissionKt")
        every {
            com.example.weatherapp.utils.ManifestPermission.checkSelfPermission(
                any(), any(), any(), any()
            )
        } answers {
            val isGrantedCallback = thirdArg<(Boolean) -> Unit>()
            isGrantedCallback(true)
        }

        every { mockApplication.applicationContext } returns mockContext
        mockkStatic(LocationServices::class)
        every { LocationServices.getFusedLocationProviderClient(any()) } returns mockFusedLocationClient

        viewModel = WeatherViewModel(mockApplication)
        viewModel.repository = mockRepository
        viewModel.fusedLocationClient = mockFusedLocationClient
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `requestWeather with location permission granted and valid response updates liveData`() = runTest {
        // Given
        val mockLocation = mock<android.location.Location> {
            on { latitude } doReturn 44.34
            on { longitude } doReturn 10.99
        }
        val locationTask: Task<android.location.Location> = Tasks.forResult(mockLocation)
        whenever(mockFusedLocationClient.lastLocation).thenReturn(locationTask)

        val mockWeatherResponse = mock<WeatherResponseModel> {
            on { name } doReturn "Milan"
            on { sys } doReturn mock<SysResponseModel> { on { country } doReturn "IT" }
            on { weather } doReturn listOf(mock<WeatherDetailsResponseModel> { on { main } doReturn "Clear" })
            on { main } doReturn mock<MainResponseModel> { on { temp } doReturn 295.15 }
        }
        whenever(mockRepository.getWeather(any())).thenReturn(mockWeatherResponse)

        val observer = mock<Observer<WeatherModel?>>()
        viewModel.liveWeather.observeForever(observer)

        // When
        viewModel.requestWeather()
        advanceUntilIdle() // Wait for coroutine to complete

        // Then
        val expectedWeatherModel = WeatherModel(
            name = "Milan",
            country = "IT",
            main = "Clear",
            temp = convertToCelciusFromKelvin(295.15),
            sunrise = "",
            sunset = ""
        )
        verify(observer).onChanged(eq(expectedWeatherModel))
        viewModel.liveWeather.removeObserver(observer)
    }

    @Test
    fun `requestWeather with location permission granted and null location falls back to default`() = runTest {
        // Given
        val locationTask: Task<android.location.Location> = Tasks.forResult(null)
        whenever(mockFusedLocationClient.lastLocation).thenReturn(locationTask)

        val mockWeatherResponse = mock<WeatherResponseModel> {
            on { name } doReturn "Default City"
            on { sys } doReturn mock<SysResponseModel> { on { country } doReturn "US" }
            on { weather } doReturn listOf(mock<WeatherDetailsResponseModel> { on { main } doReturn "Clouds" })
            on { main } doReturn mock<MainResponseModel> { on { temp } doReturn 285.15 }
        }
        whenever(mockRepository.getWeather(any())).thenReturn(mockWeatherResponse)

        val observer = mock<Observer<WeatherModel?>>()
        viewModel.liveWeather.observeForever(observer)

        // When
        viewModel.requestWeather()
        advanceUntilIdle()

        // Then
        val expectedWeatherModel = WeatherModel(
            name = "Default City",
            country = "US",
            main = "Clouds",
            temp = convertToCelciusFromKelvin(285.15),
            sunrise = "",
            sunset = ""
        )
        verify(observer).onChanged(eq(expectedWeatherModel))
        viewModel.liveWeather.removeObserver(observer)
    }
}