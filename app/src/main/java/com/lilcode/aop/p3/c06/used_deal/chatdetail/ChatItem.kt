package com.lilcode.aop.p3.c06.used_deal.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
){
    constructor(): this("","")
}