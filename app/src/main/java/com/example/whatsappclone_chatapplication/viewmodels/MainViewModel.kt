package com.example.whatsappclone_chatapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.whatsappclone_chatapplication.FirebaseRepository

class MainViewModel : ViewModel() {

    private val repository = FirebaseRepository.getInstance()

    // LiveData to observe whether the user is logged in or not
    val isLoggedIn: LiveData<Boolean> = repository.isLoggedIn


}