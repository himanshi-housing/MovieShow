package com.example.movieshow.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieshow.database.MovieDao
import com.example.movieshow.database.MovieItem
import com.example.movieshow.models.Movie
import com.example.movieshow.models.Response
import com.example.movieshow.network.MovieApi
import com.example.movieshow.paging.MoviePagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MovieRepo (private val movieDao: MovieDao, private val movieApi: MovieApi){
    lateinit var popular : Flow<PagingData<Movie>>
    lateinit var upcoming : Flow<PagingData<Movie>>
    lateinit var trending : Flow<PagingData<Movie>>
    lateinit var topRated : Flow<PagingData<Movie>>
    lateinit var watchList : LiveData<List<MovieItem>>
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getPopular(){
        popular = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"popular" )
        }.flow
    }

    fun getUpcoming(){
        upcoming = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"upcoming")
        }.flow
    }

    fun getTrending(){
        trending = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"trending")
        }.flow
    }

    fun getTopRated(){
        topRated = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"toprated")
        }.flow
    }

    suspend fun getMovies(){
        val def = coroutineScope.async {
            movieDao.getAll()
        }
        watchList = def.await()
    }
    fun addMovie(movie : MovieItem){
        coroutineScope.launch(Dispatchers.IO) {
            movieDao.insert(movie)
        }
    }

    fun deleteMovie(id : Int){
        coroutineScope.launch(Dispatchers.IO) {
            movieDao.deleteId(id)
        }
    }

}