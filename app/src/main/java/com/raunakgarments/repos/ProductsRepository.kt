package com.raunakgarments.repos

import com.google.gson.Gson
import com.raunakgarments.model.Product
import io.reactivex.rxjava3.core.Single
import java.net.URL

class ProductsRepository {
    fun getAllProducts(): Single<List<Product>> {
        return Single.create<List<Product>> {
            val json = URL("https://finepointmobile.com/data/products.json").readText()
            val products = Gson().fromJson(json, Array<Product>::class.java).toList()
            it.onSuccess(products)
        }
    }
}