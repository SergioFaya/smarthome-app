package com.example.smarthomeretrofit


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeretrofit.model.SmartHomeWeather
import kotlinx.android.synthetic.main.fragment_weather_history.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyWeatherRecyclerViewAdapter(
    private val mValues: List<SmartHomeWeather>,
    private val mListener: HistoryWeatherFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyWeatherRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as SmartHomeWeather
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_weather_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.temperatureView.text = item.weather.main.temp.toString()
        holder.humidityView.text = item.weather.main.humidity.toString()
        holder.pressureView.text = item.weather.main.pressure.toString()
        holder.cityView.text = item.weather.name
        holder.skyView.text = item.weather.weather.first().description
        holder.dateView.text = item.date

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val temperatureView: TextView = mView.temperatureField
        val humidityView: TextView = mView.humidityField
        val pressureView: TextView = mView.pressureField
        val cityView: TextView = mView.cityField
        val skyView: TextView = mView.skyField
        val dateView: TextView = mView.dateField
    }
}
