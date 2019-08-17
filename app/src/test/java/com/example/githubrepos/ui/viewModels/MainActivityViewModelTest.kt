package com.example.githubrepos.ui.viewModels

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.text.Editable
import android.view.View
import com.example.githubrepos.model.Pulls
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {
    val SUT: MainActivityViewModel = MainActivityViewModel()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
    }

    @Test
    fun verifyProgressBarVisible() {
        val pullObj = mock(Pulls::class.java)
        val pulls = arrayOf(pullObj)
        SUT.displayOpenPullRequests(pulls)
        assertEquals(SUT.displayLoader.get(), View.GONE)
    }

    @Test
    fun verifyProgressbarInVisible() {
        SUT.onSearchClicked()
        assertEquals(SUT.displayLoader.get(), View.VISIBLE)
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
}