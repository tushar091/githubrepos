package com.example.githubrepos.ui.viewModels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import com.example.githubrepos.Network.RxUtils
import com.example.githubrepos.constants.DEFAULT_TIME_OUT
import com.example.githubrepos.constants.EMPTY_STRING
import com.example.githubrepos.model.BaseRequest
import com.example.githubrepos.model.Pulls
import com.example.githubrepos.network.createRequest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.net.URL
import java.util.concurrent.TimeUnit

class DetailActivityViewModel : ViewModel() {
    val TAG = "DetailActivityViewModel"
    val compositeDisposable = CompositeDisposable()
    var url: String = EMPTY_STRING

    var userName = ObservableField(EMPTY_STRING)
    var body = ObservableField(EMPTY_STRING)
    var title = ObservableField(EMPTY_STRING)

    fun fetchDetails() {
        if (url.isEmpty()) {
            return
        }
        val request = BaseRequest(URL(url))
        compositeDisposable.add(
                createRequest(request, Pulls::class.java)
                        .flatMap { Observable.just(it.get()) }
                        .timeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                        .compose(RxUtils.applyNetworkExecutor())
                        .subscribe(
                                { displayPullRequests(it) },
                                {
                                    Log.e(TAG, it.message)
                                    handleErrors()
                                }
                        )
        )
    }

    fun displayPullRequests(pull: Pulls) {
        userName.set("User Name: " + pull.user.userName)
        body.set(pull.body)
        title.set(pull.title)
    }

    fun handleErrors() {

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}