package com.example.githubrepos.ui.viewModels

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.Editable
import android.util.Log
import android.view.View
import com.example.githubrepos.Network.RxUtils
import com.example.githubrepos.constants.BASE_URL
import com.example.githubrepos.constants.PULLS
import com.example.githubrepos.constants.REPOS
import com.example.githubrepos.model.BaseRequest
import com.example.githubrepos.model.Pulls
import com.example.githubrepos.network.createListRequest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.net.URL
import java.util.concurrent.TimeUnit

class MainActivityViewModel : ViewModel() {
    private val TAG = "MainActivityViewModel"
    private val compositeDisposable = CompositeDisposable()
    private val userName = ObservableField("")
    private val repoName = ObservableField("")
    val displayLoader = ObservableInt(View.GONE)

    fun fetchRepository(userName: String, repositoryName: String) {
        val url = "$BASE_URL$REPOS$userName/$repositoryName$PULLS"
        val request = BaseRequest(URL(url))
        compositeDisposable.add(
                createListRequest(request, Pulls::class.java)
                        .flatMap { Observable.just(it.get()) }
                        .timeout(30, TimeUnit.SECONDS)
                        .compose(RxUtils.applyNetworkExecutor())
                        .subscribe(
                                { displayOpenPullRequests(it) },
                                { Log.e(TAG, it.message) }
                        )
        )
    }

    fun displayOpenPullRequests(pulls: Array<Pulls>) {
        displayLoader.set(View.GONE)
        for (pull in pulls) {
            Log.d(TAG, pull.toString())
        }
    }

    fun onSearchClicked() {
        fetchRepository(userName.get().toString(), repoName.get().toString())
        displayLoader.set(View.VISIBLE)
    }

    fun onUserNameEntered(s: Editable) {
        userName.set(s.toString())
    }

    fun onRepoNameEntered(s: Editable) {
        repoName.set(s.toString())
    }
}