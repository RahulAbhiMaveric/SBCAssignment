package com.example.movieapp.repositories

import com.example.movieapp.data.ApiService
import com.example.movieapp.models.MovieDetails
import com.example.movieapp.models.MovieList
import com.example.movieapp.myutils.BaseApiResponse
import com.example.movieapp.myutils.DataState
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class MovieRepository @Inject constructor(private val apiService: ApiService) : BaseApiResponse() {
    /**
     * @param s is title of movie
     * @param type
     * @return @see DataState
     */
    suspend fun getMovie(s: String, type: String) = withContext(Dispatchers.IO) {
        DataState.Loading
        val result = apiService.getMovieList(s, type)
        if (result.isSuccessful) {
            DataState.Success(result.body())
        } else {
            DataState.Error(Exception(result.message()))
        }
    }

    /**
     * @return flow Flow<DataState<MovieDetails>>
     *     @param id which is imdb id
     */
    suspend fun getMovieDetailsFlow(id: String): Flow<DataState<MovieDetails>> {
        return flow {
            emit(DataState.Loading)
            emit(safeApiCall { apiService.getMovieDetails(id) })
        }.flowOn(Dispatchers.IO)
    }
}