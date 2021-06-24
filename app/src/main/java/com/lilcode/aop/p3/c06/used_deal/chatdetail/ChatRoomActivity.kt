package com.lilcode.aop.p3.c06.used_deal.chatdetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lilcode.aop.p3.c06.used_deal.DBKey.Companion.DB_CHAT
import com.lilcode.aop.p3.c06.used_deal.R
import com.lilcode.aop.p3.c06.used_deal.databinding.ActivityChatroomBinding
import kotlin.properties.Delegates

class ChatRoomActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatroomBinding.inflate(layoutInflater) }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val chatList = mutableListOf<ChatItem>()

    private val adapter = ChatItemAdapter()
    lateinit var chatDB : DatabaseReference
    var chatKey by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatKey = intent.getLongExtra("chatKey", -1)

        initChatDB()

        initChatListRecyclerView()

        initSendButton()

    }

    private fun initChatDB() {

        chatDB = Firebase.database.reference.child(DB_CHAT).child("$chatKey")

        chatDB.addChildEventListener(object: ChildEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem?:return

                // 채팅 추가;
                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    private fun initChatListRecyclerView() {
        binding.chatListRecyclerView.adapter = adapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initSendButton() {
        binding.sendButton.setOnClickListener {
            auth.currentUser?:return@setOnClickListener

            val chatItem = ChatItem(
                senderId = auth.currentUser!!.uid,
                message = binding.messageEditText.text.toString()
            )

            chatDB.push().setValue(chatItem)
        }
    }
}