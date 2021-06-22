package com.lilcode.aop.p3.c06.used_deal.chatList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lilcode.aop.p3.c06.used_deal.DBKey.Companion.CHILD_CHAT
import com.lilcode.aop.p3.c06.used_deal.DBKey.Companion.DB_USERS
import com.lilcode.aop.p3.c06.used_deal.R
import com.lilcode.aop.p3.c06.used_deal.databinding.FragmentChatlistBinding
import com.lilcode.aop.p3.c06.used_deal.home.ArticleAdapter

class ChatListFragment : Fragment(R.layout.fragment_chatlist) {
    private lateinit var binding: FragmentChatlistBinding
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding = FragmentChatlistBinding.bind(view)

        binding = fragmentChatlistBinding

        chatListAdapter = ChatListAdapter(onItemClicked = {
            // TODO 채팅 방으로 이동하는 코드;

        })

        chatRoomList.clear()

        binding.chatListRecyclerView.adapter = chatListAdapter
        binding.chatListRecyclerView.layoutManager=LinearLayoutManager(context)

        val firebaseUser = auth.currentUser ?: return

        val chatDB = Firebase.database.reference.child(DB_USERS).child(firebaseUser.uid).child(CHILD_CHAT)

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return
                    chatRoomList.add(model)
                }
                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // view가 새로 그려졌을 때;
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }


}