package com.abm.login.db

import KakaoAuthRepository
import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

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