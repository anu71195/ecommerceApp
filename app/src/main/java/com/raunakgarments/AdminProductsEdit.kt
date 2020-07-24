package com.raunakgarments

import android.os.Bundle
import android.util.Log.d
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin_products_edit.*
import kotlinx.android.synthetic.main.activity_admin_products_edit_content_scrolling.*
import org.jetbrains.anko.image
import org.jetbrains.anko.topPadding

class AdminProductsEdit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products_edit)
        setSupportActionBar(findViewById(R.id.activity_admin_products_edit_toolbar))
        val product = Gson().fromJson<Product>(intent.getStringExtra("product"), Product::class.java)
        activity_admin_products_edit_content_scrolling_productTitleAdmin.setText(product.title)
        activity_admin_products_edit_content_scrolling_productPriceAdmin.setText(product.price.toString())
        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.setText(product.photoUrl)
        activity_admin_products_edit_content_scrolling_productDescriptionAdmin.setText(product.description)
        Picasso.get().load(product.photoUrl).into(activity_admin_products_edit_content_scrolling_uploadedImagePreviewAdmin)

//        findViewById<CollapsingToolbarLayout>(R.id.activity_admin_products_edit_toolbar_layout).title = title
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}