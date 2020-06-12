package com.example.smarthomeretrofit

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeretrofit.model.SmartHomeWeather
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class HistoryWeatherActivity : AppCompatActivity(),
    HistoryWeatherFragment.OnListFragmentInteractionListener {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_weather)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint(0.0, 1.0),
                DataPoint(
                    10.0, 10.0
                ),
                DataPoint(
                    100.0, 20.0
                )
            )
        )
        // graph.addSeries(series)
    }

    override fun onListFragmentInteraction(item: SmartHomeWeather?) {
        // TODO("Not yet implemented")
    }
}
