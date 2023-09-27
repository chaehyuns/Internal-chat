package com.abm.chat.feature.ui

import androidx.lifecycle.ViewModel
import okhttp3.*

class FriendsViewModel : ViewModel() {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            // WebSocket이 열렸을 때 실행될 코드
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            // 메시지가 수신되었을 때 실행될 코드
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            // WebSocket이 정상적으로 닫혔을 때 실행될 코드
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            // 에러가 발생했을 때 실행될 코드
        }
    }

    init {
        // WebSocket 연결 초기화
        val request = Request.Builder().url("ws://your-websocket-url").build()
        webSocket = client.newWebSocket(request, webSocketListener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel이 종료될 때 WebSocket 연결도 종료
        webSocket?.close(1000, "Goodbye!")
    }
}