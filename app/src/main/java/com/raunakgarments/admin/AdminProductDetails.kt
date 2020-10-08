package com.raunakgarments.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.model.Product
import com.raunakgarments.model.ProductStockSync
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_details_admin.*
import java.util.*

class AdminProductDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details_admin)

        d("anurag", "I'm at product details")

        val product =
            Gson().fromJson<Product>(intent.getStringExtra("product"), Product::class.java)
        val productStockSync =
            Gson().fromJson<ProductStockSync>(
                intent.getStringExtra("productStockSync"),
                ProductStockSync::class.java
            )
        val title = product.title
        val price = product.price
        val description = product.description

        product_details_admin_EditProduct.setOnClickListener {
            var intent = Intent(this, AdminProductsEdit::class.java)
            intent.putExtra("product", Gson().toJson(product))
            this.startActivity(intent)
        }

        loadImageAndAvailabilityBanner(product, productStockSync)
        product_details_admin_product_name.text = title
        product_details_admin_productPrice.text = "\u20B9" + price
        product_details_admin_productDescription.text = description

//        product_details_admin_availability.setOnClickListener {
//            AlertDialog.Builder(this)
//                .setMessage("Hey $title is in stock!")
//                .setPositiveButton("OK") { p0, p1 ->
//                }
//                .create()
//                .show()
//        }
    }

    private fun loadImageAndAvailabilityBanner(
        product: Product,
        productStockSync: ProductStockSync
    ) {
        Picasso.get().load(product.photoUrl).into(product_details_admin_photo)

        if (productStockSync.stock == 0) {
            d(
                "AdminProductDetails",
                "loadImageAndAvailabilityBanner-Not available${product.id}"
            )
            product_details_admin_photo.alpha = 0.5F
            product_details_admin_notAvailableTextView.text = "Not Available"
            product_details_admin_notAvailableTextView.visibility = View.VISIBLE
        } else if (!isProductAvailableConditions(productStockSync)) {
            d(
                "AdminProductDetails",
                "loadImageAndAvailabilityBanner-Coming soon${product.id}"
            )
            product_details_admin_photo.alpha = 0.75F
            product_details_admin_notAvailableTextView.text = "Coming Soon"
            product_details_admin_notAvailableTextView.visibility = View.VISIBLE
        } else {
            d(
                "AdminProductDetails",
                "loadImageAndAvailabilityBanner-Available${product.id}"
            )
            product_details_admin_photo.alpha = 1F
            product_details_admin_notAvailableTextView.text = ""
            product_details_admin_notAvailableTextView.visibility = View.INVISIBLE
        }
        d(
            "AdminProductDetails",
            "loadImageAndAvailabilityBanner-${Gson().toJson(productStockSync)}"
        )
    }

    private fun isProductAvailableConditions(productStockSync: ProductStockSync): Boolean {
        return ((productStockSync.locked == "-1" || checkTimeStampStatus(
            productStockSync.timeStamp
        ) || productStockSync.locked == FirebaseAuth.getInstance().uid.toString()))
    }

    private fun checkTimeStampStatus(timeStamp: String): Boolean {
        return ((((Date().time) / 1000) - timeStamp.toLong()) > 600)
    }
}