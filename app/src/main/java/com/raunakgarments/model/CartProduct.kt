package com.raunakgarments.model

class CartProduct() {
    public var productId = ""
    public var objectId = ""

    fun populate(objectId: String, productId: String) {
        this.objectId = objectId
        this.productId = productId
    }

}