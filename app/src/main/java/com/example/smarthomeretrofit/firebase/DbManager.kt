package com.example.smarthomeretrofit.firebase

import com.example.smarthomeretrofit.model.SmartHomeWeatherHistory
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

object DbManager {

    private val db = FirebaseFirestore.getInstance()

    fun updateUserWeatherHistory(history: SmartHomeWeatherHistory) {

        db.collection("weatherHistory")
            .document(history.user)
            .delete()
            .addOnSuccessListener {
                db.collection("weatherHistory")
                    .document(history.user)
                    .set(history)
                    .addOnSuccessListener { documentReference ->
                        var ldoc = documentReference
                    }
                    .addOnFailureListener { e ->
                        var doc = e
                    }
            }
            .addOnFailureListener {
                var a = it
            }

    }

    fun getUserWeatherHistoryByEmail(email: String): Task<DocumentSnapshot> {
        val re = Regex("[^A-Za-z0-9 ]")
        val user = re.replace(email, "")
        return db.collection("weatherHistory")
            .document(user)
            .get();
    }
}