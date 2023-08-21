package com.example.movieshow.viewModels

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieshow.ConnectivityObserver
import com.example.movieshow.NetworkConnectivityObserver
import com.example.movieshow.database.MovieDao
import com.example.movieshow.database.MovieItem
import com.example.movieshow.models.Movie
import com.example.movieshow.network.MovieApi
import com.example.movieshow.paging.MoviePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MovieViewModel(private val movieDao: MovieDao, private val movieApi: MovieApi) : ViewModel() {

    var pageNo by mutableStateOf(0)
    var lastScreen  = ""
    var currentSreen =  "Watch List"
    lateinit var watchList : LiveData<List<MovieItem>>
    lateinit var popular : Flow<PagingData<Movie>>
    lateinit var upcoming : Flow<PagingData<Movie>>
    lateinit var trending : Flow<PagingData<Movie>>
    lateinit var topRated : Flow<PagingData<Movie>>


    init {
        viewModelScope.launch(Dispatchers.IO) {
            watchList = movieDao.getAll()
        }
        getAll()
    }

    fun getAll(){
        popular = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"popular" )
        }.flow.cachedIn(viewModelScope)

        upcoming = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"upcoming")
        }.flow.cachedIn(viewModelScope)

        trending = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"trending")
        }.flow.cachedIn(viewModelScope)

        topRated = Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieApi,"toprated")
        }.flow.cachedIn(viewModelScope)

    }
    fun addMovie(movie : MovieItem){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insert(movie)
        }
    }

    fun deleteMovie(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteId(id)
        }
    }

}