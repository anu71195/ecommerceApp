package com.raunakgarments

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.raunakgarments.model.Product

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val products = arrayListOf<Product>()
        for(i in 0..100) {
            products.add(Product(title = "red colored cotton t-shirt", photoUrl = "http://via.placeholder.com/200x200", price = 1.99))
        }

        recyler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ProductAdapter(products)
        }

    }
}
