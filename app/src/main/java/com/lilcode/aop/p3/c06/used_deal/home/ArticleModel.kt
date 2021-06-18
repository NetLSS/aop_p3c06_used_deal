package com.lilcode.aop.p3.c06.used_deal.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
){
    // 파이어베이스에 클래스 단위로 올리려면 인자빈생성자 필요;
    constructor(): this("","",0,"","")
}