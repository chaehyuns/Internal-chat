package com.abm.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        val editor  : SharedPreferences.Editor = sharedPreference.edit()

        kakaoAuthViewModel = ViewModelProvider(this).get(KakaoAuthViewModel::class.java)
        val dao = UserDatabase.getInstance(this).userDAO
        val repository= UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        val userViewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)

//        userViewModel.deleteAll()

        binding.kakaoRegisterViewModel = kakaoAuthViewModel
        binding.btnKakaoLogin.setOnClickListener{
            kakaoAuthViewModel.handleKakaoLogin()

        }

        kakaoAuthViewModel.accessToken.observe(this) {
            Log.d("MYTAG", "token is ${it}")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }

        kakaoAuthViewModel.userId.observe(this) {
            it?.let { id ->
                userViewModel.id = id.toLong()
                userViewModel.email = sharedPreference.getString("email","").toString()
//                Log.d("저장 확", "id is ${id} email is ${sharedPreference.getString("email","")}")
                userViewModel.password = "kakao"
                userViewModel.insertKakaoUser()
            } ?: run {
                // Handle null email
                Toast.makeText(this, "회원번호를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        kakaoAuthViewModel.userEmail.observe(this) {
            it?.let { email ->
                editor.putString("email","${email}")
                editor.apply() // data 저장
            } ?: run {
                // Handle null email
                Toast.makeText(this, "이메일을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}