package com.example.movieshow.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieshow.database.MovieItem
import com.example.movieshow.domain.repository.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepo: MovieRepo) : ViewModel(){
    var pageNo by mutableStateOf(0)
    var lastScreen  = ""
    var currentSreen =  "Watch List"
    val watchList : LiveData<List<MovieItem>>
        get() = movieRepo.watchList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getMovies()
        }
    }

    fun addMovie(movie : MovieItem){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.addMovie(movie)
        }
    }

    fun deleteMovie(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.deleteMovie(id)
        }
    }
}