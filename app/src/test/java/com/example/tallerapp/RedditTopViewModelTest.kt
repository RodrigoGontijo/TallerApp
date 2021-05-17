package com.example.tallerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.tallerapp.RedditTopConstants.Companion.LIMIT
import com.example.tallerapp.model.RedditTopChildren
import com.example.tallerapp.model.RedditTopModel
import com.example.tallerapp.model.service.RedditApiService
import com.example.tallerapp.viewmodel.RedditTopViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
class RedditTopViewModelTest {

    @Rule
    @Suppress("unused")
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @MockK
    lateinit var redditApiService: RedditApiService

    @MockK
    lateinit var redditTopListMock: Observer<RedditTopModel>

    @MockK
    lateinit var loadingMock: Observer<Boolean>

    @MockK
    lateinit var redditTopListLoadErrorMock: Observer<Boolean>

    @InjectMockKs
    lateinit var redditTopViewModel: RedditTopViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun shouldReturnSucessWhenPicsListRequest() {
        val response = RedditTopModel(RedditTopChildren((arrayListOf())))

        every { redditApiService.getRedditTopList(LIMIT, "") } answers { Single.just(response) }
        every { redditTopListMock.onChanged(any()) } answers { nothing }
        every { loadingMock.onChanged(any()) } answers { nothing }

        redditTopViewModel.redditTopList.observeForever(redditTopListMock)
        redditTopViewModel.loading.observeForever(loadingMock)
        redditTopViewModel.fetchRedditTopList(LIMIT, "")

        verify { loadingMock.onChanged(true) }
        verify { redditTopListMock.onChanged(response) }
        verify { loadingMock.onChanged(false) }
    }

    @Test
    fun shouldReturnErrorsWhenPicsListRequest() {
        val error = Exception()

        every { redditApiService.getRedditTopList(LIMIT, "")} answers { Single.error(error)}
        every { redditTopListLoadErrorMock.onChanged(any()) } answers { nothing }
        every { loadingMock.onChanged(any()) } answers { nothing }

        redditTopViewModel.redditTopError.observeForever(redditTopListLoadErrorMock)
        redditTopViewModel.loading.observeForever(loadingMock)
        redditTopViewModel.fetchRedditTopList(LIMIT, "")

        verify { loadingMock.onChanged(true) }
        verify { redditTopListLoadErrorMock.onChanged(true) }
        verify { loadingMock.onChanged(false) }
    }
}