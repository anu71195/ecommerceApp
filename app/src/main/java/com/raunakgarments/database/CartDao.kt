package com.raunakgarments.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Query("Select * from CartModel")
    fun getAll(): List<CartModel>

    @Insert
    fun InsertAll(vararg item: CartModel)
}