package com.raunakgarments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log.d
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.raunakgarments.productdetails.ProductDetailsViewModel
import com.raunakgarments.repos.ProductsRepository
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.product_details.*
import kotlinx.android.synthetic.main.product_details_admin.*
import org.jetbrains.anko.Android
import kotlin.Double.Companion.POSITIVE_INFINITY

class AdminProductDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details_admin)

        d("anurag", "I'm at product details")

        val product = Gson().fromJson<Product>(intent.getStringExtra("product"), Product::class.java)
        val title = product.title
        val price = product.price
        val description = product.description

        product_details_admin_EditProduct.setOnClickListener {
            var intent = Intent(this, AdminProductsEdit::class.java)
            intent.putExtra("product", Gson().toJson(product))
            this.startActivity(intent)
        }

        Picasso.get().load(intent.getStringExtra("imageUrl")).into(product_details_admin_photo)
        product_details_admin_product_name.text = title
        product_details_admin_productPrice.text = "\u20B9" + price
        product_details_admin_productDescription.text = description

        product_details_admin_availability.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Hey $title is in stock!")
                .setPositiveButton("OK") { p0, p1 ->
                }
                .create()
                .show()
        }
    }
}