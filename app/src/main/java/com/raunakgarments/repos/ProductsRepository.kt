package com.raunakgarments.repos

import com.google.gson.Gson
import com.raunakgarments.model.Product
import io.reactivex.rxjava3.core.Single
import java.net.URL

class ProductsRepository {
    fun getAllProducts(): Single<List<Product>> {
        return Single.create<List<Product>> {
            it.onSuccess(fetchProducts())
        }
    }

    fun searchForProducts(term: String): Single<List<Product>> {
        return Single.create<List<Product>> {
            val filteredProducts = fetchProducts().filter{it.title.contains(term,ignoreCase = true)}
            it.onSuccess(filteredProducts)
        }
    }

    fun getProductByName(name: String): Single<Product> {
        return Single.create<Product> {
            val product = fetchProducts().first {it.title == name}
            it.onSuccess(product)
        }
    }


    fun fetchProducts(): List<Product> {
        val json = URL("https://gist.githubusercontent.com/anu71195/3892796549c5e22a2ba591eb3fb723b7/raw/152e420b40578325f4c4babb3f2e84905eb1c0e5/shopping_products.json").readText()
        return Gson().fromJson(json, Array<Product>::class.java).toList()
    }
}