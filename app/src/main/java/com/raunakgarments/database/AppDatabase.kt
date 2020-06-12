package com.raunakgarments.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseProduct::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}