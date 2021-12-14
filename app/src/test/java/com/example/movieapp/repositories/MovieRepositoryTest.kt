package com.example.movieapp.repositories

import com.example.movieapp.data.ApiService
import com.example.movieapp.models.MovieDetails
import com.example.movieapp.models.MovieList
import com.example.movieapp.myutils.DataState
import junit.framework.TestCase
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
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
    fun testGetMovie() = runBlocking {
        val mockRepo = mock<MovieRepository> {
            onBlocking {
                getMovie(any(), any())
            } doReturn DataState.Success(movieList)
        }
        val response = mockRepo.getMovie("marvel", "movie")
        assert(response is DataState.Success)
    }

    @Test
    fun testGetMovieFail() = runBlocking {

        val mockRepo = mock<MovieRepository> {
            onBlocking {
                getMovie(any(), any())
            } doReturn DataState.Error(Exception(""))
        }
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
