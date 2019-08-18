package com.example.githubrepos.ui.viewModels

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.text.Editable
import android.view.View
import com.example.githubrepos.constants.LIST_SIZE_ZERO
import com.example.githubrepos.constants.RESPONSE_RECIEVED
import com.example.githubrepos.model.PullRequestHolder
import com.example.githubrepos.model.Pulls
import com.example.githubrepos.model.TYPE_LOADER
import com.example.githubrepos.model.User
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {
    val SUT: MainActivityViewModel = MainActivityViewModel()

    @Mock
    lateinit var observer: Observer<Int>

    @Mock
    lateinit var holderObserver: Observer<PullRequestHolder>

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
    }

    @Test
    fun verifyProgressBarVisible() {
        val user = User("a", "b")
        val pull = mock(Pulls::class.java)
        `when`(pull.title).then { "title" }
        `when`(pull.user).then { user }
        val pulls = arrayOf(pull)
        SUT.displayOpenPullRequests(pulls)
        assertEquals(SUT.displayLoader.get(), View.GONE)
        assertEquals(SUT.displayErrorLayout.get(), View.GONE)
        assertEquals(SUT.displayPullRequestlayout.get(), View.VISIBLE)
    }

    @Test
    fun verifyErrorLayoutVisibility() {
        SUT.handleErrors()
        assertEquals(SUT.displayLoader.get(), View.GONE)
        assertEquals(SUT.displayPullRequestlayout.get(), View.GONE)
        assertEquals(SUT.displayErrorLayout.get(), View.VISIBLE)
    }

    @Test
    fun verifyProgressbarInVisible() {
        SUT.onSearchClicked()
        assertEquals(SUT.displayLoader.get(), View.VISIBLE)
        assertEquals(SUT.displayPullRequestlayout.get(), View.GONE)
        assertEquals(SUT.displayErrorLayout.get(), View.GONE)
    }

    @Test
    fun verifyEmptyList() {
        SUT.onSearchClicked()
        assertEquals(SUT.pullRequests.value?.size, 0)
    }

    @Test
    fun verifyEdittexts() {
        val userName = mock(Editable::class.java)
        val reponame = mock(Editable::class.java)
        SUT.onRepoNameEntered(reponame)
        SUT.onUserNameEntered(userName)
        assertEquals(SUT.userName.get(), userName.toString())
        assertEquals(SUT.repoName.get(), reponame.toString())
    }

    @Captor
    var userNameCaptor: ArgumentCaptor<String>? = null

    @Captor
    var repoNameCaptor: ArgumentCaptor<String>? = null

    @Test
    fun verifyEdittextsValueInNetworkCall() {
        val model = spy(MainActivityViewModel::class.java)
        val userName = mock(Editable::class.java)
        val reponame = mock(Editable::class.java)
        model.onRepoNameEntered(reponame)
        model.onUserNameEntered(userName)
        model.onSearchClicked()
        verify(model, times(1))
                .fetchRepository(userNameCaptor?.capture() ?: "",
                        repoNameCaptor?.capture() ?: "")
        val userNameList = userNameCaptor?.allValues
        val repoNameList = repoNameCaptor?.allValues

        assertEquals(userNameList?.get(0), userName.toString())
        assertEquals(repoNameList?.get(0), reponame.toString())

    }

    @Test
    fun verifyErrorsInNetworkCall() {
        SUT.showLoader()
        assertEquals(SUT.currentPage,2)
        SUT.showLoader()
        assertEquals(SUT.currentPage,3)
    }

    @Test
    fun verifyUIActionsOnResponseRecieve(){
        SUT.uiAction.observeForever(observer)
        val user = User("a", "b")
        val pull = mock(Pulls::class.java)
        `when`(pull.title).then { "title" }
        `when`(pull.user).then { user }
        val pulls = arrayOf(pull)
        SUT.displayOpenPullRequests(pulls)
        verify(observer).onChanged(RESPONSE_RECIEVED)
    }

    @Test
    fun verifyUIActionsOnEmptyList(){
        SUT.uiAction.observeForever(observer)
        SUT.currentPage = 2
        val pulls = arrayOf<Pulls>()
        SUT.displayOpenPullRequests(pulls)
        verify(observer).onChanged(LIST_SIZE_ZERO)
    }

    @Test
    fun verifyUIActionsOnEmptyListAndFirstPage(){
        SUT.uiAction.observeForever(observer)
        SUT.currentPage = 1
        val pulls = arrayOf<Pulls>()
        SUT.displayOpenPullRequests(pulls)
        assertEquals(SUT.displayLoader.get(), View.GONE)
        assertEquals(SUT.displayPullRequestlayout.get(), View.GONE)
        assertEquals(SUT.displayErrorLayout.get(), View.VISIBLE)
    }

    @Test
    fun verifyShowLoader(){
        SUT.loadRequest.observeForever(holderObserver)
        SUT.showLoader()
        verify(holderObserver).onChanged(PullRequestHolder("","", TYPE_LOADER))
    }
}