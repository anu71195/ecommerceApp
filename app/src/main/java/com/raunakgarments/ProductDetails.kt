package com.raunakgarments

import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

        val title = intent.getStringExtra("title")
        val price = intent.getDoubleExtra("price", POSITIVE_INFINITY)
//        Picasso.get().load(intent.getStringExtra("imageURL")).into(photo)
        product_name.text = title +"\n price = \u20B9" + price

        val product = ProductsRepository().getProductByName(title)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                product_name.text = it.title
                Picasso.get().load(it.photoUrl).into(photo)
                productPrice.text = "₹${it.price}"
            },{})


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