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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.btnSignUp.setOnClickListener {
            val inputEmail = binding.userEmail.text.toString()
            val inputPassword = binding.userPw.text.toString()

            when {
                inputEmail.isBlank() -> {
                    Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                inputPassword.isBlank() -> {
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                !inputEmail.isEmailValid() -> { // 이메일 형식 확인
                    Toast.makeText(this, "유효한 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    userViewModel.getUserByEmail(inputEmail).observe(this) { user ->
                        if (user != null) {
                            // 이메일 이미 데이터베이스에 존재
                            Toast.makeText(this, "이미 가입된 이메일 입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            // 회원가입 성공
                            userViewModel.email = inputEmail
                            userViewModel.password = inputPassword
                            userViewModel.loginType = LoginType.LOCAL
                            userViewModel.insertUser()

                            saveUserDataToPreferences(inputEmail)

                            Toast.makeText(this, "회원가입에 성공했습니다!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

    }

    fun String.isEmailValid(): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return emailRegex.matches(this)
    }

    private fun setupViewModel() {
        val dao = UserDatabase.getInstance(this).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
    }

    private fun saveUserDataToPreferences(email: String) {
        val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putString("id", "${userViewModel.id}")
        editor.putString("email", email)
        editor.apply()
    }

    // edit text가 아닌 다른 곳을 클릭할시 키보드 내려감
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}
