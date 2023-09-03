package com.example.movieshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieshow.repository.MovieRepo
import com.example.movieshow.viewModels.MovieViewModel
import com.example.movieshow.viewModels.UpcompingViewModel

@Suppress("UNCHECKED_CAST")
class UpcomingViewModeFactory(private val repo: MovieRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UpcompingViewModel::class.java)){
            return UpcompingViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel not created")
    }
}
