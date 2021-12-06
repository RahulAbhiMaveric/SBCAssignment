package com.example.movieapp.myutils

import retrofit2.Response

/**
 * @BaseApiResponse is abstract class which handles generic Response type
 *
 */
abstract class BaseApiResponse {
    /**
     * @param takes suspend lamda function and @return
     * generic DataState class
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): DataState<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return DataState.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }
    private fun <T> error(errorMessage: String): DataState<T> =
        DataState.Error(java.lang.Exception(errorMessage))
}