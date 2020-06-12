package com.example.smarthomeretrofit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeretrofit.model.User
import com.example.smarthomeretrofit.model.enum.Keys
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        auth = FirebaseAuth.getInstance()
        autoLogin();
    }


    @SuppressLint("SourceLockedOrientationActivity")
    private fun autoLogin() {
        val sharedPreferences =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
        val json: String? = sharedPreferences.getString(Keys.USER_SMARTHOME.value, null)
        if (json != null) {
            val user = User()
            user.deserialize(json)
            loginUser(user.email, user.password)
        } else {
            setContentView(R.layout.activity_main)
            supportActionBar!!.hide();

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            btnLogin.setOnClickListener {
                loginUser(emailEditText.text.toString(), passwordEditText.text.toString());
            };

            btnRegister.setOnClickListener {
                createUser(emailEditText.text.toString(), passwordEditText.text.toString())
            };
        }
    }


    private fun loginUser(email: String, password: String) {
        validateUserAndPass(email, password)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                authorize();
            }
            .addOnCanceledListener {
                Toast.makeText(
                    this,
                    "La cuenta para ese usuario y contraseña no existe.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun validateUserAndPass(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "El campo usuario está vacío", Toast.LENGTH_LONG).show()
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "El campo password está vacío", Toast.LENGTH_LONG).show()
        }

        if (password.length < 6) {
            Toast.makeText(this, "El campo password debe tener 6 caracteres", Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun authorize() {
        finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun createUser(email: String, password: String) {
        validateUserAndPass(email, password)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                authorize()
            }
            .addOnCanceledListener {
                Toast.makeText(
                    this,
                    "No se ha podido crear el usuario. Ha usado ese email para otra cuenta?",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
