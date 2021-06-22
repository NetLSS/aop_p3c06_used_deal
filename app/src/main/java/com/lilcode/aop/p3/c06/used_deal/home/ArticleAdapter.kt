package com.lilcode.aop.p3.c06.used_deal.home


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lilcode.aop.p3.c06.used_deal.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(val onItemClicked: (ArticleModel) -> Unit) : ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(articleModel: ArticleModel) {

            // Long 형식에서 날짜로 바꾸기.
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(articleModel.createdAt)

            binding.titleTextView.text = articleModel.title
            binding.dateTextView.text = format.format(date).toString()
            binding.priceTextView.text = articleModel.price

            // glide로 이미지 불러오기;
            if (articleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(articleModel.imageUrl)
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                onItemClicked(articleModel)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                // 현재 노출하고 있는 아이템과 새로운 아이템이 같은지 비교;
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                // equals 비교;
                return oldItem == newItem

            }

        }
    }
}