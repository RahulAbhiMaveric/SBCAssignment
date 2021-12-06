package com.example.movieapp.di.module

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.ApiService
import com.example.movieapp.myutils.MyConstant.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module class for api calling and retrofit instance creation
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * returns base url
     */
    @Provides
    fun provideBaseUrl() = BASE_URL

    /**
     * returns @see OkHttpClient with logging interceptor for debugging
     * api calling and response
     * An OkHttp interceptor which logs request and response information
     */
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(TokenInterceptorModule())
            .build()
    }else{
        OkHttpClient
            .Builder()
            .addInterceptor(TokenInterceptorModule())
            .build()
    }

    /**
     * returns @See Retrofit builder
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    /**
     * retruns @return Retrofit instance for api calling
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

}