package com.abm.chat.feature.ui

import com.abm.chat.feature.data.repository.KakaoAuthRepository
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.abm.chat.core.Constants.GOOGLE_CLIENT_ID
import com.abm.chat.databinding.ActivityMainBinding
import com.abm.chat.feature.data.datasource.local.*
import com.abm.chat.feature.data.factory.KakaoAuthViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var kakaoAuthViewModel: KakaoAuthViewModel
    private lateinit var userViewModel: UserViewModel

    private val googleSignInClient: GoogleSignInClient by lazy {
        setupGoogleSignInClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupViewModels()
        setupObservers()
        handleUserActions()
    }

    private fun setupUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clearUserPreferences()
    }

    private fun setupGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(GOOGLE_CLIENT_ID)
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    private fun setupViewModels() {
        val kakaoAuthRepository = KakaoAuthRepository(this)
        val kakaoAuthViewModelFactory = KakaoAuthViewModelFactory(this.application, kakaoAuthRepository)
        kakaoAuthViewModel = ViewModelProvider(this, kakaoAuthViewModelFactory).get(KakaoAuthViewModel::class.java)

        val dao = UserDatabase.getInstance(this).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding.kakaoRegisterViewModel = kakaoAuthViewModel
        binding.lifecycleOwner = this
    }

    private fun setupObservers() {
        // For Kakao login success
        kakaoAuthViewModel.accessToken.observe(this) {
            navigateToHome()
        }

        // For Kakao user details
        kakaoAuthViewModel.userDetail.observe(this) { detail ->
            detail?.let { processKakaoUserDetails(it) } ?: run {
                Toast.makeText(this, "카카오 로그인 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleUserActions() {
        binding.btnSignUp.setOnClickListener {
            navigateToSignUp()
        }

        binding.btnLogin.setOnClickListener {
            performLocalLogin()
        }

        binding.googleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun clearUserPreferences() {
        val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
        sharedPreference.edit().clear().apply()
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun performLocalLogin() {
        val inputEmail = binding.userId.text.toString().trim()
        val inputPassword = binding.userPw.text.toString().trim()

        if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        userViewModel.login(inputEmail, inputPassword).observe(this) { user ->
            user?.let { onSuccessLocalLogin(it, inputEmail) } ?: onFailedLocalLogin()
        }
    }

    private fun onSuccessLocalLogin(user: User, inputEmail: String) {
        val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
        sharedPreference.edit().putString("id", "${user.id}").putString("email", inputEmail).apply()

        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
        navigateToHome()
    }

    private fun onFailedLocalLogin() {
        Toast.makeText(this, "이메일 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun processKakaoUserDetails(detail: UserDetail) {
        userViewModel.id = detail.id!!
        userViewModel.email = detail.email ?: "email is null"
        userViewModel.loginType = LoginType.KAKAO
        userViewModel.password = "kakao"
        userViewModel.insertUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            handleGoogleSignInResult(data)
        }
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            navigateToHome()
            account?.email?.let {
                //Save email
            }
        } catch (e: ApiException) {
            //Toast.makeText(this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}