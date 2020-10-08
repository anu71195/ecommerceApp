package com.raunakgarments.NU.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseProduct::class, CartModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}