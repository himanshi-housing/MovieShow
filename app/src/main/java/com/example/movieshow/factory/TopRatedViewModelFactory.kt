package com.example.movieshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieshow.repository.MovieRepo
import com.example.movieshow.viewModels.MovieViewModel
import com.example.movieshow.viewModels.TopRatedViewModel

@Suppress("UNCHECKED_CAST")
class TopRatedViewModelFactory(private val repo: MovieRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TopRatedViewModel::class.java)){
            return TopRatedViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel not created")
    }
}
