package com.example.smarthomeretrofit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeretrofit.model.SmartHomeWeather
import com.example.smarthomeretrofit.model.weather.*

class HistoryWeatherFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_history_list, container, false)


        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                var list = getDataList()
                adapter = MyWeatherRecyclerViewAdapter(list, listener)
            }
        }
        return view
    }

    fun getDataList(): List<SmartHomeWeather> {

        // TODO: OBTENER LISTADO
        var list = ArrayList<SmartHomeWeather>();


        var weathers = ArrayList<Weather>()
        weathers.add(
            Weather(
                description = "Nubes",
                id = 1,
                icon = "01",
                main = "Clouds"
            )
        )
        list.add(
            SmartHomeWeather(
                WeatherResponse(
                    base = "stations",
                    main = Main(
                        temp = 25.0,
                        humidity = 35,
                        pressure = 100,
                        feels_like = .0,
                        temp_max = .0,
                        temp_min = .0
                    ),
                    weather = weathers,
                    clouds = Clouds(1),
                    cod = 1,
                    coord = Coord(
                        .0,
                        .0
                    ),
                    id = 1,
                    name = "Madrid",
                    sys = Sys(
                        country = "Spain",
                        id = 1,
                        sunrise = 1,
                        sunset = 1,
                        type = 1
                    ),
                    dt = 1,
                    timezone = 1,
                    visibility = 1,
                    wind = Wind(
                        deg = 1,
                        speed = .0
                    )
                )


            )
        )
        list.add(
            SmartHomeWeather(
                WeatherResponse(
                    base = "stations",
                    main = Main(
                        temp = 25.0,
                        humidity = 35,
                        pressure = 100,
                        feels_like = .0,
                        temp_max = .0,
                        temp_min = .0
                    ),
                    weather = weathers,
                    clouds = Clouds(1),
                    cod = 1,
                    coord = Coord(
                        .0,
                        .0
                    ),
                    id = 1,
                    name = "Madrid",
                    sys = Sys(
                        country = "Spain",
                        id = 1,
                        sunrise = 1,
                        sunset = 1,
                        type = 1
                    ),
                    dt = 1,
                    timezone = 1,
                    visibility = 1,
                    wind = Wind(
                        deg = 1,
                        speed = .0
                    )
                )


            )
        )
        list.add(
            SmartHomeWeather(
                WeatherResponse(
                    base = "stations",
                    main = Main(
                        temp = 25.0,
                        humidity = 35,
                        pressure = 100,
                        feels_like = .0,
                        temp_max = .0,
                        temp_min = .0
                    ),
                    weather = weathers,
                    clouds = Clouds(1),
                    cod = 1,
                    coord = Coord(
                        .0,
                        .0
                    ),
                    id = 1,
                    name = "Madrid",
                    sys = Sys(
                        country = "Spain",
                        id = 1,
                        sunrise = 1,
                        sunset = 1,
                        type = 1
                    ),
                    dt = 1,
                    timezone = 1,
                    visibility = 1,
                    wind = Wind(
                        deg = 1,
                        speed = .0
                    )
                )


            )
        )
        list.add(
            SmartHomeWeather(
                WeatherResponse(
                    base = "stations",
                    main = Main(
                        temp = 25.0,
                        humidity = 35,
                        pressure = 100,
                        feels_like = .0,
                        temp_max = .0,
                        temp_min = .0
                    ),
                    weather = weathers,
                    clouds = Clouds(1),
                    cod = 1,
                    coord = Coord(
                        .0,
                        .0
                    ),
                    id = 1,
                    name = "Madrid",
                    sys = Sys(
                        country = "Spain",
                        id = 1,
                        sunrise = 1,
                        sunset = 1,
                        type = 1
                    ),
                    dt = 1,
                    timezone = 1,
                    visibility = 1,
                    wind = Wind(
                        deg = 1,
                        speed = .0
                    )
                )


            )
        )
        return list;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: SmartHomeWeather?)
    }
}