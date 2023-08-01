package com.example.movieshow

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

class MovieViewModel(private val movieDao: MovieDao) : ViewModel() {

    var pageNo by mutableStateOf(0)
    var lastScreen  = "Landing Page"
    var popular  : List<Movie> by mutableStateOf(listOf())
    var upcoming  : List<Movie> by mutableStateOf(listOf())
    var trending  : List<Movie> by mutableStateOf(listOf())
    var topRated  : List<Movie> by mutableStateOf(listOf())
    var scrollState : MutableList<LazyListState> = MutableList(4){LazyListState(0,0)}
//    var watchList : List<MovieItem> by mutableStateOf(listOf())
    lateinit var watchList : LiveData<List<MovieItem>>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            watchList = movieDao.getAll()
        }
    }
//    fun getWatchlistMovie(){
//        viewModelScope.launch(Dispatchers.IO) {
//                watchList = movieDao.getAll()
//        }
//    }
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
    fun getPopular(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                popular = apiService.getPopularMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
    fun getUpcoming(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                upcoming = apiService.getUpcomingMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
    fun getTrending(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                trending = apiService.getTrendingMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
    fun getTopRated(){
        viewModelScope.launch {
            val apiService = MovieApi.getInstance()
            try {
                topRated = apiService.getTopRatedMovies().results
            }
            catch (ex: Exception){
                throw Exception(ex.message)
            }
        }
    }
}