package com.example.movieshow.viewModels

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.movieshow.database.MovieDao
import com.example.movieshow.database.MovieItem
import com.example.movieshow.network.MovieApi
import com.example.movieshow.paging.MoviePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(private val movieDao: MovieDao, private val movieApi: MovieApi) : ViewModel() {

    var pageNo by mutableStateOf(0)
    var lastScreen  = "Landing Page"
    var scrollState : MutableList<LazyListState> = MutableList(4){LazyListState(0,0)}
    lateinit var watchList : LiveData<List<MovieItem>>

    val popular = Pager(PagingConfig(pageSize = 20)) {
        MoviePagingSource(movieApi,"popular" )
    }.flow.cachedIn(viewModelScope)

    val upcoming = Pager(PagingConfig(pageSize = 20)) {
        MoviePagingSource(movieApi,"upcoming")
    }.flow.cachedIn(viewModelScope)

    val trending = Pager(PagingConfig(pageSize = 20)) {
        MoviePagingSource(movieApi,"trending")
    }.flow.cachedIn(viewModelScope)

    val topRated = Pager(PagingConfig(pageSize = 20)) {
        MoviePagingSource(movieApi,"toprated")
    }.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            watchList = movieDao.getAll()
        }
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