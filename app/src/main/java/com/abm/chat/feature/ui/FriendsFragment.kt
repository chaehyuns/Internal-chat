package com.abm.chat.feature.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.abm.chat.R
import com.abm.chat.core.base.BaseFragment
import com.abm.chat.databinding.FragmentChatBinding
import com.abm.chat.databinding.FragmentFriendsBinding

class FriendsFragment : BaseFragment<FragmentFriendsBinding>(R.layout.fragment_friends) {

    private val friendsViewModel: FriendsViewModel by activityViewModels()

//    private lateinit var Adapter: Adapter


    override fun setup() {
        binding?.viewModel = friendsViewModel

    }

    override fun onResume() {
        super.onResume()
    }

}