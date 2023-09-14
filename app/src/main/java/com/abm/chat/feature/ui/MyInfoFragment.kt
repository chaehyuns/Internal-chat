package com.abm.chat.feature.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.abm.chat.databinding.FragmentMyInfoBinding

class MyInfoFragment : Fragment() {

    lateinit var binding: FragmentMyInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyInfoBinding.inflate(inflater, container, false)

//        Toast.makeText(activity, "MyInfoFragment", Toast.LENGTH_SHORT).show()

        val sharedPreference = activity?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreference?.edit()

        val email = sharedPreference?.getString("email", "") ?: ""
        val id = sharedPreference?.getString("id", "") ?: ""

        binding.myId.text = "id : ${id}"
        binding.myEmail.text = "email : ${email}"

        binding.btnHome.setOnClickListener {
            binding.frm.visibility = View.GONE

        }


        return binding.root
    }
}