package com.example.movieshow.data.network

import com.example.movieshow.models.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") auth : String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YzUwZGQ5NjkwYzljNDAzNzNiM2I3MTY2ZGEzNzBmYyIsInN1YiI6IjY0YzBkNjdlZGY4NmE4MDEwNjM2ODk5YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.J43R8SLFWMJekHIw_W4mVDys9JffAatRT3Uy6YFVkZM",
        @Query("page") page : Int
    ) : Response

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Header("Authorization") auth : String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YzUwZGQ5NjkwYzljNDAzNzNiM2I3MTY2ZGEzNzBmYyIsInN1YiI6IjY0YzBkNjdlZGY4NmE4MDEwNjM2ODk5YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.J43R8SLFWMJekHIw_W4mVDys9JffAatRT3Uy6YFVkZM",
        @Query("page") page : Int
    ) : Response

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Header("Authorization") auth : String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YzUwZGQ5NjkwYzljNDAzNzNiM2I3MTY2ZGEzNzBmYyIsInN1YiI6IjY0YzBkNjdlZGY4NmE4MDEwNjM2ODk5YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.J43R8SLFWMJekHIw_W4mVDys9JffAatRT3Uy6YFVkZM",
        @Query("page") page : Int
    ) : Response

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Header("Authorization") auth : String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YzUwZGQ5NjkwYzljNDAzNzNiM2I3MTY2ZGEzNzBmYyIsInN1YiI6IjY0YzBkNjdlZGY4NmE4MDEwNjM2ODk5YyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.J43R8SLFWMJekHIw_W4mVDys9JffAatRT3Uy6YFVkZM",
        @Query("page") page : Int
    ) : Response

}