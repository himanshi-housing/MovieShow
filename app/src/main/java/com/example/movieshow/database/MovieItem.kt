package com.example.movieshow.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Watchlist")
data class MovieItem(
    @PrimaryKey(autoGenerate = false)
    var id : Int,

    @ColumnInfo(name = "Time Stamp")
    var time : Long = System.currentTimeMillis(),

    @ColumnInfo(name  = "Long Poster")
    var longPoster : String,

    @ColumnInfo(name = "Short Poster")
    var shortPoster : String,

    @ColumnInfo(name = "Title")
    var title : String,

    @ColumnInfo(name = "Description")
    var descrptn : String
)
