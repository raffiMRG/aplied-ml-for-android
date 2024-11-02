package com.dicoding.asclepius.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.CancerResponse
import com.dicoding.asclepius.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferenceViewModel: ViewModel(){
    private val _eventsResponse = MutableLiveData<CancerResponse>()

    private val _listArticleItems = MutableLiveData<List<ArticlesItem>>()
    val listEvents: LiveData<List<ArticlesItem>> = _listArticleItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailure = MutableLiveData<Boolean>()
    val isFailure: LiveData<Boolean> = _isFailure

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    companion object {
        private const val Q = "cancer"
        private const val CATEGORY = "health"
        private const val LANGUAGE = "en"
    }

    fun findEvent() {
        _isLoading.value = true
        _isFailure.value = false
        val client = ApiConfig.getApiService().getTopHedlineCancer(Q, CATEGORY, LANGUAGE, BuildConfig.API_KEY)
        client.enqueue(object : Callback<CancerResponse> {
            override fun onResponse(
                call: Call<CancerResponse>,
                response: Response<CancerResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _eventsResponse.value = it
                        val tmpListArticleItem = arrayListOf<ArticlesItem>()
                        tmpListArticleItem.clear()
                        it.articles.forEach { article ->
                            Log.d("article title", article.title)
                            if (article.title == "[Removed]") {
                                // Masukkan ke dalam _eventsResponse jika title tidak kosong
                                return@forEach
                            }
                            tmpListArticleItem.add(article)
                        }
                        _listArticleItems.value = tmpListArticleItem
                     }
                } else {
                    _responseMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<CancerResponse>, t: Throwable) {
                _isLoading.value = false
                _isFailure.value = true
                when(t.message.toString()){
                    "Unable to resolve host \"${BuildConfig.HOST}\": No address associated with hostname" -> _responseMessage.value = "Gagal Memuat Halaman, Silahkan Periksa koneksi anda"
                    else -> _responseMessage.value = t.message.toString()
                }
            }
        })
    }
}