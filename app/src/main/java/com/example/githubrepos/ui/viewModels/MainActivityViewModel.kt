package com.example.githubrepos.ui.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.Editable
import android.util.Log
import android.view.View
import com.example.githubrepos.Network.RxUtils
import com.example.githubrepos.R
import com.example.githubrepos.constants.*
import com.example.githubrepos.model.*
import com.example.githubrepos.network.createListRequest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.net.URL
import java.util.concurrent.TimeUnit

class MainActivityViewModel : ViewModel() {
    private val TAG = "MainActivityViewModel"
    private val compositeDisposable = CompositeDisposable()
    var currentPage = 1
    var list = mutableListOf<PullRequestHolder>()
    val userName = ObservableField(EMPTY_STRING)
    val repoName = ObservableField(EMPTY_STRING)
    val uiAction = MutableLiveData<Int>()
    val details = MutableLiveData<String>()

    val displayLoader = ObservableInt(View.GONE)

    val displayErrorLayout = ObservableInt(View.GONE)

    val displayPullRequestlayout = ObservableInt(View.GONE)

    val errorString = ObservableInt(R.string.empty_string)

    val pullRequests: MutableLiveData<List<PullRequestHolder>> by lazy {
        MutableLiveData<List<PullRequestHolder>>()
    }

    val loadRequest: MutableLiveData<PullRequestHolder> by lazy {
        MutableLiveData<PullRequestHolder>()
    }

    fun fetchRepository(userName: String, repositoryName: String) {
        val url = "$BASE_URL$REPOS$userName/$repositoryName$PULLS$PAGE$currentPage"
        val request = BaseRequest(URL(url))
        compositeDisposable.add(
                createListRequest(request, Pulls::class.java)
                        .flatMap { Observable.just(it.get()) }
                        .timeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                        .compose(RxUtils.applyNetworkExecutor())
                        .subscribe(
                                { displayOpenPullRequests(it) },
                                {
                                    Log.e(TAG, it.message)
                                    handleErrors()
                                }
                        )
        )
    }

    fun handleErrors() {
        errorString.set(R.string.error_text)
        displayLoader.set(View.GONE)
        displayPullRequestlayout.set(View.GONE)
        displayErrorLayout.set(View.VISIBLE)
    }

    fun displayOpenPullRequests(pulls: Array<Pulls>) {
        if (pulls.isEmpty() && currentPage == 1) {
            errorString.set(R.string.no_open_pulls)
            displayErrorLayout.set(View.VISIBLE)
            displayLoader.set(View.GONE)
            displayPullRequestlayout.set(View.GONE)
            return
        }
        if (pulls.isEmpty() && currentPage > 1) {
            uiAction.postValue(LIST_SIZE_ZERO)
            return
        }
        uiAction.postValue(RESPONSE_RECIEVED)
        displayLoader.set(View.GONE)
        displayErrorLayout.set(View.GONE)
        displayPullRequestlayout.set(View.VISIBLE)
        list = pulls.map {
            val pullHolder = PullRequestHolder(
                    title = it.title,
                    userName = it.user.userName,
                    type = TYPE_PULLS,
                    pullUrl = it.url)
            pullHolder
        }.toMutableList()
        pullRequests.postValue(list)
    }

    fun showLoader() {
        loadRequest.postValue(PullRequestHolder(EMPTY_STRING, EMPTY_STRING, TYPE_LOADER, EMPTY_STRING))
        currentPage++
    }

    fun fetchNextPage() {
        fetchRepository(userName.get().toString(), repoName.get().toString())
    }

    fun onSearchClicked() {
        list.clear()
        currentPage = 1
        uiAction.postValue(SEARCH_CLICKED)
        fetchRepository(userName.get().toString(), repoName.get().toString())
        displayLoader.set(View.VISIBLE)
        displayPullRequestlayout.set(View.GONE)
        displayErrorLayout.set(View.GONE)
        pullRequests.postValue(listOf())
    }

    fun onUserNameEntered(s: Editable) {
        userName.set(s.toString())
    }

    fun onRepoNameEntered(s: Editable) {
        repoName.set(s.toString())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onItemClicked(pull: PullRequestHolder) {
        details.postValue(pull.pullUrl)
    }
}