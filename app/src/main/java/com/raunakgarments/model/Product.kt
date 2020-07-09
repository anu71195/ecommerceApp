package com.raunakgarments.model

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

class Product() {
    lateinit var id: String
    lateinit var title: String
    var price = 0.0
    lateinit var photoUrl: String
    lateinit var description: String
}