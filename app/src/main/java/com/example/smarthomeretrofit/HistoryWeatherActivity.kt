package com.example.smarthomeretrofit

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeretrofit.model.SmartHomeWeather
import com.example.smarthomeretrofit.model.SmartHomeWeatherHistory
import com.example.smarthomeretrofit.model.User
import com.example.smarthomeretrofit.model.enum.Keys
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_history_weather.*
import java.text.SimpleDateFormat


class HistoryWeatherActivity : AppCompatActivity(),
    HistoryWeatherFragment.OnListFragmentInteractionListener {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_weather)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        graph.gridLabelRenderer.setHorizontalLabelsAngle(65)
        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this, format)

        getLineGraphSeriesForAdapter(getDataList(), humidityAdapter)

        btnPlotTemperature.setOnClickListener() {
            graph.removeAllSeries()
            getLineGraphSeriesForAdapter(getDataList(), temperatureAdapter)
        }

        btnPlotPressure.setOnClickListener() {
            graph.removeAllSeries()
            getLineGraphSeriesForAdapter(getDataList(), pressureAdapter)
        }

        btnPlotHumidity.setOnClickListener() {
            graph.removeAllSeries()
            getLineGraphSeriesForAdapter(getDataList(), humidityAdapter)
        }
    }

    val temperatureAdapter = { weather: SmartHomeWeather ->
        graph.title = "Temperatura (Cº)"
        DataPoint(weather.datetime!!.toDouble(), weather.weather.main.temp)
    }

    val pressureAdapter = { weather: SmartHomeWeather ->
        graph.title = "Presión (PA)"
        DataPoint(weather.datetime!!.toDouble(), weather.weather.main.pressure.toDouble())
    }

    val humidityAdapter = { weather: SmartHomeWeather ->
        graph.title = "Humedad (%)"
        DataPoint(weather.datetime!!.toDouble(), weather.weather.main.humidity.toDouble())
    }


    private fun getLineGraphSeriesForAdapter(
        dataList: ArrayList<SmartHomeWeather>,
        adapter: (SmartHomeWeather) -> DataPoint
    ) {
        val series = LineGraphSeries<DataPoint>()
        dataList.forEach { it ->
            series.appendData(
                adapter(it),
                true,
                dataList.size
            )
        }
        graph.addSeries(series)
    }


    override fun onListFragmentInteraction(item: SmartHomeWeather?) {
        // TODO("Not yet implemented")
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

    private fun getUserFromSharedPreferences(): String {
        val sharedPreferences =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
        val json: String? = sharedPreferences.getString(Keys.USER_SMARTHOME.value, "user")
        var user = User()
        user.deserialize(json)
        return user.email;
    }

}
