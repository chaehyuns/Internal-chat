package com.abm.chat.feature.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abm.chat.R
import com.abm.chat.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

    }

    private fun initBottomNavigation() {

        binding.mainBnv.selectedItemId = R.id.friendsFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, FriendsFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.friendsFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, FriendsFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.chatFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ChatFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MyPageFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
    }
}