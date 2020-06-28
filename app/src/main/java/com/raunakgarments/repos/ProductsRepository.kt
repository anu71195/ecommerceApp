package com.raunakgarments.repos

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.raunakgarments.model.Product
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

class ProductsRepository {
    private fun retrofit(): EcommerceApi {
        return Retrofit.Builder()
            .baseUrl("https://finepointmobile.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(EcommerceApi::class.java)
    }

    suspend fun fetchAllProductsRetrofit(): List<Product> {
        return retrofit().fetchAllProducts()
    }

    fun getAllProducts(): Single<List<Product>> {
        return Single.create<List<Product>> {
            it.onSuccess(fetchProducts())
        }
    }

    suspend fun searchForProducts(term: String): List<Product> {
//        return Single.create<List<Product>> {
             return fetchAllProductsRetrofit().filter{it.title.contains(term,ignoreCase = true)}
//            it.onSuccess(filteredProducts)
//        }
    }

//    fun getProductByName(name: String): Single<Product> {
//        return Single.create<Product> {
//            val product = fetchProducts().first {it.title == name}
//            it.onSuccess(product)
//        }
//    }


    fun fetchProducts(): List<Product> {
        val json = URL("https://gist.githubusercontent.com/anu71195/3892796549c5e22a2ba591eb3fb723b7/raw/152e420b40578325f4c4babb3f2e84905eb1c0e5/shopping_products.json").readText()
        return Gson().fromJson(json, Array<Product>::class.java).toList()
    }
}