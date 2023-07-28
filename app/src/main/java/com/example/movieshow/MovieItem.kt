package com.example.movieshow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "Watchlist")
data class MovieItem(
    @PrimaryKey(autoGenerate = false)
    var time : Long = System.currentTimeMillis(),

    @ColumnInfo(name = "Url")
    var url : String,

    @ColumnInfo(name = "Title")
    var title : String,

    @ColumnInfo(name = "Description")
    var descrptn : String
)
