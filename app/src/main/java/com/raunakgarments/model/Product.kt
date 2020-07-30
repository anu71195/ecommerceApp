package com.raunakgarments.model

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

class Product() {
    public var id = ""
    var title: String = ""
    var price = 0.0
    var photoUrl: String = ""
    var description: String = ""
    var stock = 0
    var tagArray: HashMap<String, Int> = HashMap<String, Int>()

    fun populate(title: String, price: Double, photoUrl: String, description: String) {
        this.title = title
        this.price = price
        this.photoUrl = photoUrl
        this.description = description
    }


}