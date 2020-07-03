package com.example.smarthomeretrofit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeretrofit.firebase.DbManager
import com.example.smarthomeretrofit.model.SmartHomeWeather
import com.example.smarthomeretrofit.model.SmartHomeWeatherHistory
import com.example.smarthomeretrofit.model.User
import com.example.smarthomeretrofit.model.enum.Keys
import com.example.smarthomeretrofit.model.weather.WeatherResponse
import com.example.smarthomeretrofit.service.ws.WeatherRestService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var vehicleMarker: Marker? = null
    private lateinit var self: AppCompatActivity

    var requestWeatherInfo = {
        WeatherRestService.getWeatherInCity(editCity.text.toString())
            .enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                    Log.v("retrofit open weather", "call failed")
                }

                override fun onResponse(
                    call: Call<WeatherResponse>?,
                    response: Response<WeatherResponse>?
                ) {
                    Log.v("retrofit open weather", "call success")
                    if (response!!.isSuccessful) {
                        displayWeatherInfo(response.body()!!)
                        storeWeather(response.body()!!)
                    } else {
                        Toast.makeText(
                            self,
                            "Ciudad no encontrada. Utilizamos datos de openWeatherApp, por favor compruebe que la ciudad se encuentre registrada o use otra.",
                            Toast.LENGTH_LONG
                        ).show();
                    }
                }
            })
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        loadCityFromSharedPreferences()
        requestWeatherInfo()

        btnSelectCity.setOnClickListener {
            if (TextUtils.isEmpty(editCity.text.toString())) {
                Toast.makeText(this, "El campo ciudad no puede estar vacío", Toast.LENGTH_SHORT)
                    .show()
            } else {
                storeCitySharedPreferences();
                setUpUploadWeatherInfoPeriodically(requestWeatherInfo);
            }
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryWeatherActivity::class.java)
            startActivity(intent)
        }

        btnDevices.setOnClickListener {
            val intent = Intent(this, DevicesActivity::class.java)
            startActivity(intent)
        }
        // added reference to context
        self = this;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }


    private fun displayWeatherInfo(response: WeatherResponse) {
        if (mMap != null) {
            placeMarkerInLocation(response.coord.lat, response.coord.lon)
            setTemperature(response.main.temp.toString())
            setClima(response.weather.first().description)
            setPressure(response.main.pressure.toString())
            setHumidity(response.main.humidity.toString())
            loadIcon(response.weather.first().icon)
        }
    }


    private fun placeMarkerInLocation(lat: Double, lon: Double) {
        vehicleMarker?.remove()
        val location = LatLng(lat, lon)
        vehicleMarker = mMap.addMarker(
            MarkerOptions().position(location).title(
                getString(R.string.your_home)
            )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18.0f))
    }

    private fun setTemperature(text: String) {
        temperatureLabel.text = "Temperatura: " + text + "ºC"

    }

    private fun setClima(text: String) {
        weatherLabel.text = "Tiempo: " + text
    }

    private fun setPressure(text: String) {
        pressureLabel.text = "Presión: " + text + " Pa"
    }

    private fun setHumidity(text: String) {
        humidityLabel.text = "Humedad: " + text + "%"
    }

    private fun loadIcon(code: String) {
        val url = "http://openweathermap.org/img/wn/" + code + "@2x.png"
        webView.loadUrl(url)
    }

    private fun loadCityFromSharedPreferences() {
        val sharedPreferences =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
        val city: String? = sharedPreferences.getString(Keys.CITY_SMARTHOME.value, null)
        if (city != null) {
            editCity.setText(city)
        }
    }

    private fun getUserFromSharedPreferences(): String {
        val sharedPreferences =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
        val json: String? = sharedPreferences.getString(Keys.USER_SMARTHOME.value, "user")
        var user = User()
        user.deserialize(json)
        return user.email;
    }

    private fun storeCitySharedPreferences() {
        val sharedPreferences =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Keys.CITY_SMARTHOME.value, editCity.text.toString())
            .commit()
    }

    private fun storeWeather(weatherResponse: WeatherResponse): SmartHomeWeatherHistory {
        var currentWeatherHistory = getDataList()
        currentWeatherHistory.add(SmartHomeWeather(weatherResponse))

        var weatherHistory = SmartHomeWeatherHistory(getUserFromSharedPreferences())
        weatherHistory.weathers = currentWeatherHistory
        storeWeatherFirebase(weatherHistory)

        storeWeatherSharedPreferences(weatherHistory);
        return weatherHistory
    }

    private fun storeWeatherFirebase(weatherHistory: SmartHomeWeatherHistory) {
        DbManager.updateUserWeatherHistory(weatherHistory)
    }

    private fun storeWeatherSharedPreferences(weatherHistory: SmartHomeWeatherHistory) {
        var json = weatherHistory.serialize()

        getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE).edit()
            .putString(Keys.HISTORY_SMARTHOME.value, json).commit();

    }

    fun getDataList(): ArrayList<SmartHomeWeather> {
        val jsonHistory =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
                ?.getString(Keys.HISTORY_SMARTHOME.value, null)

        if (jsonHistory != null) {
            val history = SmartHomeWeatherHistory(getUserFromSharedPreferences())
            history.deserialize(jsonHistory)

            if (history != null) {
                return history.weathers!!
            }
        }
        return ArrayList<SmartHomeWeather>()
    }

    private fun setUpUploadWeatherInfoPeriodically(task: () -> Unit) {
        val handler = Handler()
        val delay = 600000L //10 minutes in milliseconds
        handler.removeCallbacksAndMessages(null);
        // execute once and stablish the interval
        task();
        handler.postDelayed(object : Runnable {
            override fun run() {
                task();
                handler.postDelayed(this, delay)
            }
        }, delay)
    }
}
