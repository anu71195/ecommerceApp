package com.raunakgarments.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("Select * from DatabaseProduct")
    fun getAll(): List<DatabaseProduct>

    @Query("Select * from DatabaseProduct where title like :term")
    fun searchFor(term: String) : List<DatabaseProduct>

    @Insert
    fun InsertAll(vararg products: DatabaseProduct)
}