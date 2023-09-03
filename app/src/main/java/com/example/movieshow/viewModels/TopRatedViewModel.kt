package com.example.movieshow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.movieshow.models.Movie
import com.example.movieshow.repository.MovieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TopRatedViewModel (private val repository : MovieRepo) : ViewModel(){
    val topRated : Flow<PagingData<Movie>>
        get() = repository.topRated

    init {
        getMovie()
    }

    fun getMovie(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTopRated()
        }
    }
}