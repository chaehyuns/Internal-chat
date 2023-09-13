package com.abm.login

import KakaoAuthRepository
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abm.login.db.KakaoAuthViewModel

class KakaoAuthViewModelFactory(
    private val application: Application,
    private val repository: KakaoAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KakaoAuthViewModel::class.java)) {
            return KakaoAuthViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}