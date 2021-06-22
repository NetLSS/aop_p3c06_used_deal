package com.lilcode.aop.p3.c06.used_deal.chatdetail

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

class ChatRoomActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatroomBinding.inflate(layoutInflater) }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val chatList = mutableListOf<ChatItem>()

    private val adapter = ChatItemAdapter()
    lateinit var chatDB : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val chatKey = intent.getLongExtra("chatKey", -1)

        chatDB = Firebase.database.reference.child(DB_CHAT).child("$chatKey")
        chatDB.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem?:return

                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        binding.chatListRecyclerView.adapter = adapter
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(this)

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