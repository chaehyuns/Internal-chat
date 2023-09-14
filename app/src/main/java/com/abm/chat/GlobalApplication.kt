package com.abm.chat

import android.app.Application
import com.abm.chat.BuildConfig.KAKAO_API_KEY
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    companion object {
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, KAKAO_API_KEY)

    }
}