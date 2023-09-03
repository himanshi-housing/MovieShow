package com.example.movieshow.viewModels

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
import com.example.movieshow.database.MovieDao
import com.example.movieshow.database.MovieItem
import com.example.movieshow.models.Movie
import com.example.movieshow.network.MovieApi
import com.example.movieshow.paging.MoviePagingSource
import com.example.movieshow.repository.MovieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MovieViewModel(private val repository : MovieRepo) : ViewModel() {

    var pageNo by mutableStateOf(0)
    var lastScreen  = ""
    var currentSreen =  "Watch List"
    val watchList : LiveData<List<MovieItem>>
        get() = repository.watchList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovies()
        }
    }

    fun addMovie(movie : MovieItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movie)
        }
    }

    fun deleteMovie(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMovie(id)
        }
    }
}