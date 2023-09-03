package com.example.movieshow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.movieshow.models.Movie
import com.example.movieshow.repository.MovieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PopularViewModel(private val repository : MovieRepo) : ViewModel(){
    val popular : Flow<PagingData<Movie>>
        get() = repository.popular

    init {
        getMovie()
    }

    fun getMovie(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPopular()
        }
    }
}