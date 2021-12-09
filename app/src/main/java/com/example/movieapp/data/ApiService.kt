package com.example.movieapp.data

import com.example.movieapp.models.MovieDetails
import com.example.movieapp.models.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * api service class for network request
 */
interface ApiService {
    /**
     * returns movie list from server from given title
     * @param s is title
     * @param type e. marvel,comedy etc
     */
    @GET(".")
    suspend fun getMovieList(
        @Query("s") text: String,
        @Query("type") type: String
    ): Response<MovieList>

    /**
     * retruns Response<MovieDetals>
     *     @param i is imdb id
     */
    @GET(".")
    suspend fun getMovieDetails(@Query("i") id: String): Response<MovieDetails>
    /**
     * pagination url example
     * http://www.omdbapi.com/?apikey=b9bd48a6&s=Batman&movie=marvel&page=2
     */
}