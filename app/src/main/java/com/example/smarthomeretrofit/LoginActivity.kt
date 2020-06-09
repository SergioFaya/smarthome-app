package com.example.smarthomeretrofit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeretrofit.model.User
import com.example.smarthomeretrofit.model.enum.Keys
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                goToRegister()
            };
        }
    }


    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    authorize();
                } else {
                    Toast.makeText(
                        this,
                        "La cuenta para ese usuario y contraseña no existe.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun authorize() {
        finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun goToRegister() {
        finish()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
