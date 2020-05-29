package com.raunakgarments

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_details.*
import kotlin.Double.Companion.POSITIVE_INFINITY

class ProductDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)

        val title = intent.getStringExtra("title")
        val price = intent.getDoubleExtra("price", POSITIVE_INFINITY)
        Picasso.get().load(intent.getStringExtra("imageURL")).into(photo)
        product_name.text = title +"\n price = \u20B9" + price
    }
}