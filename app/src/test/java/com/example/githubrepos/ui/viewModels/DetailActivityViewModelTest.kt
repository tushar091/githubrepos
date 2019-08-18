package com.example.githubrepos.ui.viewModels

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.view.View
import com.example.githubrepos.constants.EMPTY_STRING
import com.example.githubrepos.model.Pulls
import com.example.githubrepos.model.User
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DetailActivityViewModelTest {
    val SUT: DetailActivityViewModel = DetailActivityViewModel()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
    }

    @Test
    fun verifyEmptyUrl() {
        SUT.url = EMPTY_STRING
        SUT.fetchDetails()
        assertEquals(SUT.error.get(), View.VISIBLE)
    }

    @Test
    fun verifyPullRequests() {
        val name = "anyname"
        val imageUrl = "anyUrl"
        val body = "anubody"
        val user = mock(User::class.java)
        val pull = mock(Pulls::class.java)
        `when`(pull.title).then { "title" }
        `when`(pull.user).then { user }
        `when`(user.userName).then { name }
        `when`(user.avatarUrl).then { imageUrl }
        `when`(pull.body).then { body }
        SUT.displayPullRequests(pull)
        assertEquals(SUT.userName.get(), name)
        assertEquals(SUT.imageUrl.get(), imageUrl)
        assertEquals(SUT.title.get(), "title")
        assertEquals(SUT.body.get(), body)
    }

}