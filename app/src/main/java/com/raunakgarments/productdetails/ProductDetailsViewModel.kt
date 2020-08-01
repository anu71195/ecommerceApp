//package com.raunakgarments.productdetails
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.gson.Gson
//import com.raunakgarments.model.Product
//import com.raunakgarments.repos.ProductsRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.net.URL
//
//class ProductDetailsViewModel : ViewModel() {
//
//    val productDetails = MutableLiveData<Product>()
//
//    fun fetchProductDetails(productTitle: String) {
//        viewModelScope.launch(Dispatchers.Default) {
//            productDetails.postValue(ProductsRepository().fetchProduct(productTitle))
////            val json =
////                URL("https://gist.githubusercontent.com/anu71195/3892796549c5e22a2ba591eb3fb723b7/raw/152e420b40578325f4c4babb3f2e84905eb1c0e5/shopping_products.json").readText()
////            val list = Gson().fromJson(json, Array<Product>::class.java).toList()
////            val product = list.first { it.title == productTitle }
////
////            productDetails.postValue(product)
//        }
//    }
//}