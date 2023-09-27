package com.abm.chat.feature.ui

import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.abm.chat.R
import com.abm.chat.core.base.BaseFragment
import com.abm.chat.data.repository.user.datasource.local.UserDatabase
import com.abm.chat.data.repository.user.datasource.local.UserRepository
import com.abm.chat.data.repository.user.datasource.local.UserViewModel
import com.abm.chat.data.repository.user.datasource.local.UserViewModelFactory
import com.abm.chat.databinding.FragmentMyPageBinding

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun setup() {
        binding?.viewModel = myPageViewModel

        val sharedPreference = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        val dao = UserDatabase.getInstance(requireContext()).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        val userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding?.btnDeleteAll?.setOnClickListener {
            userViewModel.deleteAll()
            Toast.makeText(requireContext(), "모든 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            editor.clear()
            editor.apply()
            requireActivity().finish()
        }

        binding?.btnMyInfo?.setOnClickListener {
            binding?.frm?.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frm, MyInfoFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding?.btnLogout?.setOnClickListener {
            Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            editor.clear()
            editor.apply()
            requireActivity().finish()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }
}