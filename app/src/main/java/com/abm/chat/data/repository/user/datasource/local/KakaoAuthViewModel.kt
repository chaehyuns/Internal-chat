package com.abm.chat.data.repository.user.datasource.local

import com.abm.chat.data.repository.user.KakaoAuthRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class KakaoAuthViewModel(application: Application, private val kakaoAuthRepository: KakaoAuthRepository) : AndroidViewModel(application) {

    val accessToken = MutableLiveData<String?>()
    val userDetail = MutableLiveData<UserDetail>()
    val loginError = MutableLiveData<String?>()

    fun handleKakaoLogin() {
        kakaoAuthRepository.kakaoLogin(
            onTokenReceived = { token ->
                accessToken.value = token.accessToken
                fetchUserDetail()
            },
            onError = { error ->
                loginError.value = "Login failed: $error"
            }
        )
    }

    private fun fetchUserDetail() {
        kakaoAuthRepository.getUserDetail(
            onSuccess = { user ->
                userDetail.value = UserDetail(user.id, user.email)
            },
            onError = { error ->
                loginError.value = "Failed to fetch user detail: $error"
            }
        )
    }
}