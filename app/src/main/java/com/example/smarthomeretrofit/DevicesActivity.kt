package com.example.smarthomeretrofit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeretrofit.model.enum.Keys
import com.example.smarthomeretrofit.model.lightbulb.Color
import com.example.smarthomeretrofit.model.lightbulb.Fade
import com.example.smarthomeretrofit.model.lightbulb.Light
import com.example.smarthomeretrofit.model.speaker.Speaker
import com.example.smarthomeretrofit.model.weather.WeatherResponse
import com.example.smarthomeretrofit.service.ws.LightBulbRestService
import com.example.smarthomeretrofit.service.ws.SpeakerRestService
import com.example.smarthomeretrofit.service.ws.WeatherRestService
import kotlinx.android.synthetic.main.activity_devices.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DevicesActivity : AppCompatActivity() {

    private lateinit var self: AppCompatActivity

    private val red = Color(230, 31, 28)
    private val yellow = Color(237, 234, 45)
    private val blue = Color(52, 174, 235)

    private val tone1 = 1;
    private val tone2 = 2;

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setUpLightButtonEvents()
        setUpSpeakerButtonEvents()
        setUpSimulatorDebug()
        setUpAutoButton()
        self = this;
    }

    private fun setUpLightButtonEvents() {
        setLightOn()
        setLightOff()
        setColorBtn()
        setLightFade()
        setRainbow();
    }

    private fun setUpSpeakerButtonEvents() {
        setSpeakerBeep()
        setSpeakerMelody()
    }

    private fun setLightOn() {
        btnOn.setOnClickListener {
            it.isEnabled = false
            LightBulbRestService.lampOn(this)
                .enqueue(object :
                    Callback<Light> {
                    override fun onFailure(call: Call<Light>?, t: Throwable?) {
                        Log.v("retrofit light", "call failed")
                        it.isEnabled = true
                    }

                    override fun onResponse(
                        call: Call<Light>?,
                        response: Response<Light>?
                    ) {
                        Log.v("retrofit light", "call success")
                        if (response!!.isSuccessful) {
                            Toast.makeText(
                                self, "LAMPARA ON", Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                self, "No se ha podido encender la lámpara", Toast.LENGTH_LONG
                            ).show();
                        }
                        it.isEnabled = true
                    }
                })
        }
    }

    private fun setLightOff() {
        btnOff.setOnClickListener {
            it.isEnabled = false
            LightBulbRestService.lampOff(this)
                .enqueue(object :
                    Callback<Light> {
                    override fun onFailure(call: Call<Light>?, t: Throwable?) {
                        Log.v("retrofit light", "call failed")
                        it.isEnabled = true
                    }

                    override fun onResponse(
                        call: Call<Light>?,
                        response: Response<Light>?
                    ) {
                        Log.v("retrofit light", "call success")
                        if (response!!.isSuccessful) {
                            Toast.makeText(
                                self, "LAMPARA OFF", Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                self, "No se ha podido apagar la lámpara", Toast.LENGTH_LONG
                            ).show();
                        }
                        it.isEnabled = true
                    }


                })
        }
    }

    private fun setColorBtn() {
        btnSetColor.setOnClickListener {
            it.isEnabled = false
            var red = 255;
            var green = 255;
            var blue = 255;
            try {
                red = Integer.parseInt(editRedColor.text.toString())
                green = Integer.parseInt(editGreenColor.text.toString())
                blue = Integer.parseInt(editBlueColor.text.toString())

                val valid = validateColors(red, green, blue)
                if (valid) {
                    LightBulbRestService.setLampColor(
                        this,
                        Color(
                            r = red,
                            b = blue,
                            g = green
                        )
                    )
                        .enqueue(object :
                            Callback<Light> {
                            override fun onFailure(call: Call<Light>?, t: Throwable?) {
                                Log.v("retrofit light", "call failed")
                                it.isEnabled = true
                            }

                            override fun onResponse(
                                call: Call<Light>?,
                                response: Response<Light>?
                            ) {
                                Log.v("retrofit light", "call success")
                                if (response!!.isSuccessful) {
                                    Toast.makeText(
                                        self,
                                        "RGB: Actualizado",
                                        Toast.LENGTH_LONG
                                    ).show();
                                } else {
                                    Toast.makeText(
                                        self,
                                        "No se ha podido establecer el color de la lámpara",
                                        Toast.LENGTH_LONG
                                    ).show();
                                }
                                it.isEnabled = true
                            }
                        })
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(self, "Número de color no válido", Toast.LENGTH_LONG).show();
                it.isEnabled = true
            }
        }
    }

    private fun validateColors(r: Int, g: Int, b: Int): Boolean {
        if (r < 0 || g < 0 || b < 0) {
            Toast.makeText(this, "El formato de colores debe ser de 0 a 255", Toast.LENGTH_LONG)
                .show()
            return false;
        }
        if (r > 255 || g > 255 || b > 255) {
            Toast.makeText(this, "El formato de colores debe ser de 0 a 255", Toast.LENGTH_LONG)
                .show()
            return false;
        }
        return true
    }

    private fun setLightFade() {
        btnFade.setOnClickListener {
            it.isEnabled = false
            var delay = 0;
            var numberOfRepetitions = 0;
            try {
                delay = Integer.parseInt(editDelay.text.toString())
                numberOfRepetitions = Integer.parseInt(editNRepetition.text.toString())

                val valid = validateFade(delay, numberOfRepetitions)
                if (valid) {
                    LightBulbRestService.setLampFade(
                        this,
                        Fade(
                            d = delay,
                            n = numberOfRepetitions
                        )
                    )
                        .enqueue(object :
                            Callback<Light> {
                            override fun onFailure(call: Call<Light>?, t: Throwable?) {
                                Log.v("retrofit light", "call failed")
                                it.isEnabled = true
                            }

                            override fun onResponse(
                                call: Call<Light>?,
                                response: Response<Light>?
                            ) {
                                Log.v("retrofit light", "call success")
                                if (response!!.isSuccessful) {
                                    Toast.makeText(
                                        self,
                                        "RGB: Modo Fade",
                                        Toast.LENGTH_LONG
                                    ).show();
                                } else {
                                    Toast.makeText(
                                        self,
                                        "No se ha podido activar el efecto de la lámpara",
                                        Toast.LENGTH_LONG
                                    ).show();
                                }
                                it.isEnabled = true
                            }
                        })
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(self, "Número de gradiente no válido", Toast.LENGTH_LONG).show();
                it.isEnabled = true
            }
        }
    }

    private fun validateFade(d: Int, n: Int): Boolean {
        if (d < 0) {
            Toast.makeText(this, "El formato de delay debe ser mayor que 0", Toast.LENGTH_LONG)
                .show()
            return false;
        }
        if (n < 0) {
            Toast.makeText(
                this,
                "El formato de repeticiones debe ser mayor que 0",
                Toast.LENGTH_LONG
            )
                .show()
            return false;
        }
        return true
    }

    private fun setRainbow() {
        btnRainbow.setOnClickListener {
            it.isEnabled = false
            LightBulbRestService.setLampRainbow(this)
                .enqueue(object :
                    Callback<Light> {
                    override fun onFailure(call: Call<Light>?, t: Throwable?) {
                        Log.v("retrofit light", "call failed")
                        it.isEnabled = true
                    }

                    override fun onResponse(
                        call: Call<Light>?,
                        response: Response<Light>?
                    ) {
                        Log.v("retrofit light", "call success")
                        if (response!!.isSuccessful) {
                            Toast.makeText(
                                self, "TOOGLE RAINBOW", Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                self, "No se ha podido activar el modo rainbow", Toast.LENGTH_LONG
                            ).show();
                        }
                        it.isEnabled = true
                    }
                })
        }
    }

    private fun setSpeakerBeep() {
        btnBeep.setOnClickListener {
            it.isEnabled = false
            SpeakerRestService.speakerBeep(this)
                .enqueue(object :
                    Callback<Speaker> {
                    override fun onFailure(call: Call<Speaker>?, t: Throwable?) {
                        Log.v("retrofit speaker", "call failed")
                        it.isEnabled = true
                    }

                    override fun onResponse(
                        call: Call<Speaker>?,
                        response: Response<Speaker>?
                    ) {
                        Log.v("retrofit speaker", "call success")
                        if (response!!.isSuccessful) {
                            Toast.makeText(
                                self, "Altavoz modo pitido", Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                self,
                                "No se ha podido activar el pitido del altavoz",
                                Toast.LENGTH_LONG
                            ).show();
                        }
                        it.isEnabled = true
                    }
                })
        }
    }

    private fun setSpeakerMelody() {
        btnMelody.setOnClickListener {
            it.isEnabled = false
            var melody = 0;
            try {
                melody = Integer.parseInt(editMelody.text.toString())

                val valid = validateMelody(melody)
                if (valid) {
                    SpeakerRestService.speakerMelody(this, melody)
                        .enqueue(object :
                            Callback<Speaker> {
                            override fun onFailure(call: Call<Speaker>?, t: Throwable?) {
                                Log.v("retrofit speaker", "call failed")
                                it.isEnabled = true
                            }

                            override fun onResponse(
                                call: Call<Speaker>?,
                                response: Response<Speaker>?
                            ) {
                                Log.v("retrofit speaker", "call success")
                                if (response!!.isSuccessful) {
                                    Toast.makeText(
                                        self,
                                        "Altavoz modo melodía",
                                        Toast.LENGTH_LONG
                                    ).show();
                                } else {
                                    Toast.makeText(
                                        self,
                                        "No se ha podido activar el modo de melodía del altavoz",
                                        Toast.LENGTH_LONG
                                    ).show();
                                }
                                it.isEnabled = true
                            }
                        })
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(self, "Número de melodía no válido", Toast.LENGTH_LONG).show();
                it.isEnabled = true
            }
        }
    }

    private fun validateMelody(tono: Int): Boolean {
        if (tono < 0) {
            Toast.makeText(this, "La melodía debe ser mayor que 0", Toast.LENGTH_LONG)
                .show()
            return false;
        }
        return true
    }

    private fun setUpSimulatorDebug() {
        btnOpenSimulationLink.setOnClickListener {
            val uri: Uri =
                Uri.parse(getString(R.string.base_url_device))
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun setUpAutoButton() {
        btnAutoMode.setOnClickListener {


            it.isEnabled = false

            var city = getCity()
            WeatherRestService.getWeatherInCity(city)
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
                            var weather = response.body()
                            autoLight(weather!!)

                        } else {
                            Toast.makeText(
                                self,
                                "Ciudad no encontrada. Utilizamos datos de openWeatherApp, por favor compruebe que la ciudad se encuentre registrada o use otra.",
                                Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                })
            it.isEnabled = true
        }
    }

    private fun autoLight(weather: WeatherResponse) {
        if (weather.main.temp < 15 && weather.main.humidity < 35) {
            Toast.makeText(this, "Frío seco. Luz azul y melodía 1", Toast.LENGTH_LONG).show()
            setLampMode(blue, tone1)
        } else if (weather.main.temp < 15 && weather.main.humidity >= 35) {
            Toast.makeText(this, "Frío humedo. Luz azul y melodía 2", Toast.LENGTH_LONG).show()
            setLampMode(blue, tone2)
        } else if (weather.main.temp >= 15 && weather.main.temp < 22 && weather.main.humidity < 35) {
            Toast.makeText(
                this,
                "Temperatura Ambiente Seca. Luz amarilla y melodía 1",
                Toast.LENGTH_LONG
            ).show()
            setLampMode(yellow, tone1)
        } else if (weather.main.temp >= 15 && weather.main.temp < 22 && weather.main.humidity >= 35) {
            Toast.makeText(
                this,
                "Temperatura Ambiente Humeda. Luz amarilla y melodía 2",
                Toast.LENGTH_LONG
            ).show()
            setLampMode(yellow, tone2)
        } else if (weather.main.temp >= 22 && weather.main.humidity < 35) {
            Toast.makeText(this, "Temperatura Cálida. Luz roja y melodía 1", Toast.LENGTH_LONG)
                .show()
            setLampMode(red, tone1)
        } else if (weather.main.temp >= 22 && weather.main.humidity >= 35) {
            Toast.makeText(this, "Temperatura Cálida. Luz roja y melodía 2", Toast.LENGTH_LONG)
                .show()
            setLampMode(red, tone2)
        } else {
            Toast.makeText(
                this,
                "No hay un modo establecido para el clima actual",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun setLampMode(color: Color, tone: Int) {
        val thread = Thread(Runnable {
            LightBulbRestService.setLampColor(this, color).execute()
            SpeakerRestService.speakerMelody(this, tone).execute()
        })
        thread.start()
    }


    private fun getCity(): String {
        val sharedPreferences =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
        val city: String? = sharedPreferences.getString(Keys.CITY_SMARTHOME.value, null)
        if (city != null) {
            return city;
        }
        return "";
    }
}
