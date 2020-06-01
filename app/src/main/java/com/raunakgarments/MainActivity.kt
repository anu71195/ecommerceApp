package com.raunakgarments

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raunakgarments.model.Product

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            drawerLayout.closeDrawers()
            true

        }

        val products = arrayListOf<Product>()
        val imageUrl = "https://5.imimg.com/data5/YJ/WF/MY-28712927/mens-t-shirt-500x500.jpg"
        for(i in 0..100) {
            products.add(Product(title = "red colored cotton t-shirt #$i", photoUrl = imageUrl, price = 1.99))
        }

        recyler_view.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = ProductAdapter(products)
        }

    }
}
