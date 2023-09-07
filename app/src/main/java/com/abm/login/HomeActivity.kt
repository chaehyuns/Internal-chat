package com.abm.login

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.abm.login.databinding.ActivityHomeBinding
import com.abm.login.databinding.ActivityMainBinding
import com.abm.login.db.UserDatabase
import com.abm.login.db.UserRepository
import com.abm.login.db.UserViewModel
import com.abm.login.db.UserViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
        val editor  : SharedPreferences.Editor = sharedPreference.edit()
        val dao = UserDatabase.getInstance(this).userDAO
        val repository= UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        val userViewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)

//        var email = sharedPreference.getString("email","").toString()
//        var id = sharedPreference.getString("id","").toString()
//        binding.string2.text = "회원 정보 확인 \nid : ${id}\nemail : ${email} "

        binding.btnDeleteAll.setOnClickListener {
            userViewModel.deleteAll()
            Toast.makeText(this, "모든 회원 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            editor.clear()
            editor.apply()
            finish()
        }

        binding.btnMyInfo.setOnClickListener {
            binding.frm.visibility = View.VISIBLE // frm의 가시성을 VISIBLE로 변경
            supportFragmentManager.beginTransaction()
                .replace(R.id.frm, MyInfoFragment())
                .commitAllowingStateLoss()
        }


    }
}