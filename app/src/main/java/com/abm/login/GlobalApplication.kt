package com.abm.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    companion object {
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "05b640b80be2d741cae452a978f6e671")

    }
}