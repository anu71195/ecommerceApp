package com.raunakgarments.repos

import com.google.gson.Gson
import com.raunakgarments.model.Product
import io.reactivex.rxjava3.core.Single
import java.net.URL

class ProductsRepository {
    fun getAllProducts(): Single<List<Product>> {
        return Single.create<List<Product>> {
            val json = URL("https://gist.githubusercontent.com/anu71195/3892796549c5e22a2ba591eb3fb723b7/raw/152e420b40578325f4c4babb3f2e84905eb1c0e5/shopping_products.json").readText()
            val products = Gson().fromJson(json, Array<Product>::class.java).toList()
            it.onSuccess(products)
        }
    }
}