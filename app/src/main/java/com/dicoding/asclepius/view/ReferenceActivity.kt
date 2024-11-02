package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.Adapter
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityReferenceBinding
import com.dicoding.asclepius.model.ReferenceViewModel

class ReferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReferenceBinding
    private lateinit var referenceViewModel: ReferenceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        referenceViewModel = ViewModelProvider(this)[ReferenceViewModel::class.java]
        binding = ActivityReferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkModel()

        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvListCancerNews.layoutManager = layoutManager
        }

        referenceViewModel.listEvents.observe(this) { events ->
            setReviewData(events)
        }

        referenceViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        referenceViewModel.findEvent()

    }
    private fun setReviewData(events: List<ArticlesItem>) {
        val adapter = Adapter()
        adapter.submitList(events)
        binding.rvListCancerNews.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun checkModel() {
        referenceViewModel.isFailure.observe(this) {
            if (it) {
                binding.messageStatus.visibility = View.VISIBLE
                binding.rvListCancerNews.visibility = View.INVISIBLE
                referenceViewModel.responseMessage.observe(this) { message ->
                    binding.messageStatus.text = message
                }
            }
        }
    }
}