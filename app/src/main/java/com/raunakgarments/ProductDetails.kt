package com.raunakgarments

import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log.d
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.raunakgarments.productdetails.ProductDetailsViewModel
import com.raunakgarments.repos.ProductsRepository
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.product_details.*
import org.jetbrains.anko.Android
import kotlin.Double.Companion.POSITIVE_INFINITY

class ProductDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)

        val title = intent.getStringExtra("title") ?: ""
        val price = intent.getDoubleExtra("price", POSITIVE_INFINITY)

        addToCartButton.setOnClickListener {
            d("cart button", "is working")
        }

        Picasso.get().load(intent.getStringExtra("imageUrl")).into(photo)
        product_name.text = title
        productPrice.text = "\u20B9" + price

        availability.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Hey $title is in stock!")
                .setPositiveButton("OK") { p0, p1 ->
                }
                .create()
                .show()
        }
    }
}