package com.raunakgarments.model

open class ConfirmationCartProduct() {
    public var id = ""
    var title: String = ""
    var price = 0.0
    var photoUrl: String = ""
    var description: String = ""
    var quantity = 1.0
    var totalPrice = 0.0
    var productStatus = 0
    var tagArray: HashMap<String, Int> = HashMap<String, Int>()

}