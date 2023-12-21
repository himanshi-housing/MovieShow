package com.example.movieshow.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.movieshow.database.MovieItem
import com.example.movieshow.models.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepo {
    abstract val trending: Flow<PagingData<Movie>>
    abstract val upcoming: Flow<PagingData<Movie>>
    abstract val topRated: Flow<PagingData<Movie>>
    abstract val popular: Flow<PagingData<Movie>>
    abstract val watchList: LiveData<List<MovieItem>>

    fun getPopular() : Flow<PagingData<Movie>>
    fun getUpcoming() : Flow<PagingData<Movie>>
    fun getTrending() : Flow<PagingData<Movie>>

    fun getTopRated() : Flow<PagingData<Movie>>
    suspend fun getMovies() : LiveData<List<MovieItem>>
    fun addMovie(movie : MovieItem)
    fun deleteMovie(id :Int)
}