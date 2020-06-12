package com.raunakgarments.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseProduct(

    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo val title: String,
    @ColumnInfo val price: Double

)