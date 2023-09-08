package com.abm.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.abm.login.databinding.ActivityMainBinding
import com.abm.login.db.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var kakaoAuthViewModel: KakaoAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreference.edit()

        editor.clear()
        editor.apply()

        kakaoAuthViewModel = ViewModelProvider(this).get(KakaoAuthViewModel::class.java)
        val dao = UserDatabase.getInstance(this).userDAO
        val repository= UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        val userViewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)

        binding.kakaoRegisterViewModel = kakaoAuthViewModel
        binding.btnKakaoLogin.setOnClickListener{
            kakaoAuthViewModel.handleKakaoLogin()

        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        kakaoAuthViewModel.accessToken.observe(this) {
            Log.d("MYTAG", "token is ${it}")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }

        //카카오 로그인 성공 시 유저 정보 저장 (live data 객체)
        kakaoAuthViewModel.userDetail.observe(this) { detail ->
            detail?.let {
                userViewModel.id = detail.id!!
                userViewModel.email = detail.email?:"email is null"
                userViewModel.loginType = LoginType.KAKAO
                Log.d("저장 확인", "id is ${userViewModel.id} email is ${userViewModel.email}")
                userViewModel.password = "kakao"
                userViewModel.insertUser()
            }?: run {
                // Handle null email
                Toast.makeText(this, "카카오 로그인 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        //Local 로그인

        binding.btnLogin.setOnClickListener {
            val inputEmail = binding.userId.text.toString().trim()
            val inputPassword = binding.userPw.text.toString().trim()

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.login(inputEmail, inputPassword).observe(this) { user ->
                if (user != null) {
                    // 로그인 성공
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                } else {
                    // 로그인 실패
                    Toast.makeText(this, "이메일 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }




    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}