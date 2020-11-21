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

    fun copyAsUserOrderProduct(confirmationCartProduct: ConfirmationCartProduct): UserOrderProduct {
        var userOrderedProduct = UserOrderProduct()
        userOrderedProduct.id = confirmationCartProduct.id
        userOrderedProduct.title = confirmationCartProduct.title
        userOrderedProduct.price = confirmationCartProduct.price
        userOrderedProduct.photoUrl = confirmationCartProduct.photoUrl
        userOrderedProduct.description = confirmationCartProduct.description
        userOrderedProduct.quantity = confirmationCartProduct.quantity
        userOrderedProduct.totalPrice = confirmationCartProduct.totalPrice
        userOrderedProduct.productStatus = confirmationCartProduct.productStatus
        userOrderedProduct.tagArray = confirmationCartProduct.tagArray

        return userOrderedProduct
    }

}