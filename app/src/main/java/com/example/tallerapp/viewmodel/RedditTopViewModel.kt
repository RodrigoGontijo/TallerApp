package com.example.tallerapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.tallerapp.model.RedditTopModel
import com.example.tallerapp.model.service.RedditApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class RedditTopViewModel (
    private val redditApiService: RedditApiService
) : BaseViewModel() {

    val redditTopList = MutableLiveData<RedditTopModel>()
    val redditTopError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun fetchRedditTopList(limit: Int, after: String) {
        loading.value = true
        disposable.add(
            redditApiService.getRedditTopList(limit, after)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RedditTopModel>() {
                    override fun onSuccess(redditTopModel: RedditTopModel) {
                        redditTopList.value = redditTopModel
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        redditTopError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }
}