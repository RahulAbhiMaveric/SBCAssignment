package com.example.movieapp.repositories

import com.example.movieapp.data.ApiService
import com.example.movieapp.models.MovieDetails
import com.example.movieapp.models.MovieList
import com.example.movieapp.myutils.DataState
import junit.framework.TestCase
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import retrofit2.Response


@RunWith(JUnit4::class)
class MovieRepositoryTest {
    private lateinit var movieList: MovieList
    private lateinit var movieDetails: MovieDetails
    private lateinit var mockRepo: MovieRepository
    private lateinit var apiService: ApiService

    @Before
    fun init() {
        apiService = mock<ApiService>()
        mockRepo = MovieRepository(apiService)
        movieList = mock<MovieList>()
        movieDetails = mock<MovieDetails>()
    }

    @Test
    fun testGetMovie(): Unit = runBlocking {
        Mockito.`when`(apiService.getMovieList(any(), any()))
            .thenReturn(Response.success(movieList))

        val response = mockRepo.getMovie("marvel", "movie")
        assert(response is DataState.Success)
        verify(apiService).getMovieList(any(), any())
    }

    @Test
    fun testGetMovieFail(): Unit = runBlocking {

        Mockito.`when`(apiService.getMovieList(any(), any()))
            .thenThrow(ArrayIndexOutOfBoundsException("error"))

        val response = mockRepo.getMovie("marvel", "movie")
        assert(response is DataState.Error)
        verify(apiService).getMovieList(any(), any())
    }

    @Test
    fun testMovieError(): Unit = runBlocking {
        val resBody = mock<ResponseBody>()
        Mockito.`when`(apiService.getMovieList(any(), any()))
            .thenReturn(Response.error(404, resBody))

        val response = mockRepo.getMovie("marvel", "movie")
        assert(response is DataState.Error)
    }

    @Test
    fun testGetMovieDetailsFlow() = runBlocking {
        Mockito.`when`(apiService.getMovieDetails(any())).thenReturn(Response.success(movieDetails))
        val firstItem = mockRepo.getMovieDetailsFlow("tt4154664").first()
        val secondItem = mockRepo.getMovieDetailsFlow("tt4154664").drop(1).first()
        TestCase.assertTrue(firstItem is DataState.Loading)
        TestCase.assertTrue(secondItem is DataState.Success)
    }

    @Test
    fun testGetMovieDetailsFlowError() = runBlocking {
        Mockito.`when`(apiService.getMovieDetails(any()))
            .thenThrow(ArrayIndexOutOfBoundsException("error"))
        val firstItem = mockRepo.getMovieDetailsFlow("tt4154664").first()
        val secondItem = mockRepo.getMovieDetailsFlow("tt4154664").drop(1).first()
        TestCase.assertTrue(firstItem is DataState.Loading)
        TestCase.assertTrue(secondItem is DataState.Error)
    }

}
