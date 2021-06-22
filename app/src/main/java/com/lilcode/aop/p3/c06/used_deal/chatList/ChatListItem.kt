package com.lilcode.aop.p3.c06.used_deal.chatList

data class ChatListItem(
    val buyerId: String,
    val sellerId: String,
    val itemTitle: String,
    val key: Long
){
    constructor():this("","","",0)
}
