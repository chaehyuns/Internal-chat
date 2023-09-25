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

class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private val chatViewModel: ChatViewModel by activityViewModels()

//    private lateinit var myPageKeywordAdapter: MyPageKeywordAdapter


    override fun setup() {
        binding?.viewModel = chatViewModel

//        val adapter = MessageAdapter() // TODO: 채팅 메시지를 표시하기 위한 어댑터 및 recyclerView 설정
//        binding.recyclerView.adapter = adapter

//        chatViewModel.messages.observe(this, { messages ->
//            adapter.submitList(messages)
//        })

        binding?.btnSend?.setOnClickListener {
            val message = binding?.editMessage?.text.toString()
            if (message.isNotBlank()) {
                chatViewModel.sendMessage(message)
                binding?.editMessage?.text?.clear()
            }
        }

//        setKeywordAdapter()
//        setNavigateClickListener()
//        setKeywordObserver()
    }

    override fun onResume() {
        super.onResume()
    }

}