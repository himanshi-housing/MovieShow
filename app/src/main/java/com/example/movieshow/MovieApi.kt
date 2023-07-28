package com.example.movieshow

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Header

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") auth : String = API_KEY
    ) : Response

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Header("Authorization") auth : String = API_KEY,
    ) : Response

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Header("Authorization") auth : String = API_KEY
    ) : Response

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Header("Authorization") auth : String = API_KEY
    ) : Response

    companion object{
        var apiService : MovieApi ?= null

         fun getInstance() : MovieApi{
             if(apiService==null){
                 apiService = Retrofit.Builder()
                     .baseUrl("https://api.themoviedb.org/3/")
                     .addConverterFactory(GsonConverterFactory.create())
                     .build().create(MovieApi::class.java)
             }
             return apiService!!
        }
    }
}