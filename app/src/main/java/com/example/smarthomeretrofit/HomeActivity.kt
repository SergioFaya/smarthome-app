package com.example.smarthomeretrofit

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeretrofit.model.weather.WeatherResponse
import com.example.smarthomeretrofit.service.WeatherService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    private val appId = "d307a57cbe751709269280ea4f1b5527";
    private val units = "metric"
    private val lang = "es"

    private lateinit var mMap: GoogleMap
    private var vehicleMarker: Marker? = null
    private lateinit var self: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnSelectCity.setOnClickListener {
            if (TextUtils.isEmpty(editCity.text.toString())) {
                Toast.makeText(this, "El campo ciudad no puede estar vacío", Toast.LENGTH_SHORT)
                    .show()
            } else {
                requestWeatherInfo();
            }
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryWeatherActivity::class.java)
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

    fun requestWeatherInfo() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service: WeatherService = retrofit.create(WeatherService::class.java)

        val call = service.getWeatherInCity(editCity.text.toString(), appId, lang, units)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                Log.v("retrofit open weather", "call failed")
            }

            override fun onResponse(
                call: Call<WeatherResponse>?,
                response: Response<WeatherResponse>?
            ) {
                Log.v("retrofit open weather", "call success")
                if (response!!.isSuccessful) {
                    displayWeatherInfo(response.body()!!);
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
        humidityLabel.text = "Humedad: " + text + " "
    }

    private fun loadIcon(code: String) {
        val url = "http://openweathermap.org/img/wn/" + code + "@2x.png"
        webView.loadUrl(url)
    }
}
