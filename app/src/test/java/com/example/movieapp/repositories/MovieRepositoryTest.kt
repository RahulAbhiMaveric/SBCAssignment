package com.example.movieapp.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieapp.data.ApiService
import com.example.movieapp.di.module.TokenInterceptorModule
import com.example.movieapp.models.MovieList
import com.example.movieapp.myutils.DataState
import com.example.movieapp.myutils.MyConstant.Companion.BASE_URL
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class MovieRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var repository: MovieRepository
    private val mockRepo=Mockito.mock(MovieRepository::class.java)
    private val server = MockWebServer()
    private  lateinit var movieList : MovieList
    @Before
    fun init() {
        movieList=MovieList()
        movieList=Mockito.mock(MovieList::class.java)
        server.start(8000)
        //val baseUrl = server.url("/").toString()
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(TokenInterceptorModule())
            .build()
        val service = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(ApiService::class.java)
        repository = MovieRepository(service)
    }

    @Test
    fun testGetMovie() {
        server.enqueue(
            MockResponse()
        )
        Mockito.`when`(runBlocking { mockRepo.getMovie("marvel", "movie") }).thenReturn(DataState.Success(movieList))
        val response = runBlocking { mockRepo.getMovie("marvel", "movie") }

        Assert.assertNotNull(response)
        TestCase.assertEquals(response, DataState.Success(movieList))
    }

    @Test
    fun testGetMovieDetailsFlow() {
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}
