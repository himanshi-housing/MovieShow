package com.example.movieshow.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.movieshow.domain.repository.MovieRepo
import com.example.movieshow.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(private val movieRepo: MovieRepo) : ViewModel() {
    val topRated : Flow<PagingData<Movie>>
        get() = movieRepo.topRated

    init {
        getMovie()
    }

    fun getMovie(){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getTopRated()
        }
    }
}