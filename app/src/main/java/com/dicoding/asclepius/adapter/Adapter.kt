package com.dicoding.asclepius.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemRowBinding
import com.dicoding.asclepius.view.DetailsActivity

class Adapter : ListAdapter<ArticlesItem, Adapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    class MyViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem){
            Glide.with(binding.root)
                .load(article.urlToImage)
                .into(binding.itemImage)
            binding.itemTitle.text = article.title
            binding.itemDescription.text = article.description
            this.itemView.setOnClickListener{
                val intentDetail = Intent(this.itemView.context, DetailsActivity::class.java)
                intentDetail.putExtra("key_image", article.urlToImage)
                intentDetail.putExtra("key_title", article.title)
                intentDetail.putExtra("key_description", article.description)
                this.itemView.context.startActivity(intentDetail)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}