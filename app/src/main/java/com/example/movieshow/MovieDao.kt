package com.example.movieshow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * from Watchlist ORDER BY `Time Stamp` DESC")
    fun getAll() : LiveData<List<MovieItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item:MovieItem)

    @Query("DELETE FROM Watchlist where id = :id")
    suspend fun deleteId(id: Int)
}