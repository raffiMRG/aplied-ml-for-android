package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataImage = intent.getStringExtra("key_image")
        val dataTitle = intent.getStringExtra("key_title")
        val dataDescription = intent.getStringExtra("key_description")

        Glide.with(binding.root)
            .load(dataImage)
            .into(binding.detailImage)
        binding.detailTitle.text = dataTitle
        binding.detailDescription.text = dataDescription
    }
}