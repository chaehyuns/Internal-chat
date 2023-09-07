package com.abm.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.abm.login.databinding.ActivitySignUpBinding
import com.abm.login.db.*
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()

        val dao = UserDatabase.getInstance(this).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        val userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding.btnSignUp.setOnClickListener {
            //입력 저장
            var InputEmail = ""
            var InputPassword = ""
            binding.apply {
                InputEmail = userEmail.text.toString()
                InputPassword = userPw.text.toString()
            }

            userViewModel.email = InputEmail
            userViewModel.password = InputPassword
            userViewModel.loginType = LoginType.LOCAL
            userViewModel.insertUser()


            editor.putString("id","${userViewModel.id}")
            editor.putString("email","${InputEmail}")
            editor.apply() // data 저장

            //회원가입 성공
            Toast.makeText(this, "회원가입에 성공했습니다!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }
    }

    //edit text가 아닌 다른 곳을 클릭할시 키보드 내려감
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }



}