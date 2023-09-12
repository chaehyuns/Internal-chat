import android.content.Context
import android.content.SharedPreferences
import com.abm.login.db.UserDetail
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class KakaoAuthRepository(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun kakaoLogin(onTokenReceived: (OAuthToken) -> Unit, onError: (Throwable) -> Unit) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                onError(error)
            } else if (token != null) {
                onTokenReceived(token)
                saveUserDetails(token)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun saveUserDetails(token: OAuthToken) {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("id", "${user.id}")
                editor.putString("email", "${user.kakaoAccount?.email}")
                editor.apply()
            }
        }
    }

    fun getUserDetail(onSuccess: (UserDetail) -> Unit, onError: (Throwable) -> Unit) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                onError(error)
            } else if (user != null) {
                onSuccess(UserDetail(user.id, user.kakaoAccount?.email))
            }
        }
    }
}