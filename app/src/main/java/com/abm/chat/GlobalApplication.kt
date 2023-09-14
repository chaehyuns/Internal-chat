package com.abm.chat

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    val KAKAO_API_KEY = BuildConfig.KAKAO_API_KEY
    companion object {
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, KAKAO_API_KEY)

    }
}