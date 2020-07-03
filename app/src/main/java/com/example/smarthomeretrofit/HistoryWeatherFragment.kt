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
import com.example.smarthomeretrofit.model.SmartHomeWeatherHistory
import com.example.smarthomeretrofit.model.User
import com.example.smarthomeretrofit.model.enum.Keys

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

    fun getDataList(): ArrayList<SmartHomeWeather> {
        val jsonHistory =
            activity!!.getSharedPreferences(
                Keys.USER_SHARED_PREFERENCES.value,
                Context.MODE_PRIVATE
            )
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
            activity!!.getSharedPreferences(
                Keys.USER_SHARED_PREFERENCES.value,
                Context.MODE_PRIVATE
            )
        val json: String? = sharedPreferences.getString(Keys.USER_SMARTHOME.value, "user")
        var user = User()
        user.deserialize(json)
        return user.email;
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