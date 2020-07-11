package com.raunakgarments.model

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

class Product() {
    public var id = ""
    lateinit var title: String
    var price = 0.0
    lateinit var photoUrl: String
    var description: String = ""

    fun populate(title: String, price: Double, photoUrl: String, description: String) {
        this.title = title
        this.price = price
        this.photoUrl = photoUrl
        this.description = description
    }


}