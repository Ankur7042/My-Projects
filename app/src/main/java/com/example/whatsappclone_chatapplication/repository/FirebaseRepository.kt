package com.example.whatsappclone_chatapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository private constructor() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

     val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

     val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    init {
        // Initialize the login status
        _isLoggedIn.value = auth.currentUser != null

        // Listen for changes in authentication state
        auth.addAuthStateListener { firebaseAuth ->
            _isLoggedIn.value = firebaseAuth.currentUser != null
        }
    }

    companion object {
        @Volatile
        private var instance: FirebaseRepository? = null

        fun getInstance(): FirebaseRepository {
            return instance ?: synchronized(this) {
                instance ?: FirebaseRepository().also { instance = it }
            }
        }
    }

}
