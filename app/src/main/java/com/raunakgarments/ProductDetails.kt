package com.raunakgarments

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.raunakgarments.model.ProductStockSync
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_details.*
import java.util.*
import kotlin.Double.Companion.POSITIVE_INFINITY

class ProductDetails : AppCompatActivity() {
    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var canProductBeAdded = true
    lateinit var userId: String
    lateinit var firebaseUtil: FirebaseUtil
    lateinit var mDatabaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details)

        mFirebaseAuth = FirebaseAuth.getInstance()
        this.userId = mFirebaseAuth.uid.toString()

        firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("userCart/" + this.userId)
        mDatabaseReference = firebaseUtil.mDatabaseReference

        d("anurag", "I'm at product details")

        val product =
            Gson().fromJson<Product>(intent.getStringExtra("product"), Product::class.java)
        val productStockSync =
            Gson().fromJson<ProductStockSync>(
                intent.getStringExtra("productStockSync"),
                ProductStockSync::class.java
            )
        d(
            "ProductDetails",
            "onCreate-${Gson().toJson(productStockSync)}"
        )
        val title = intent.getStringExtra("title") ?: ""
        val price = intent.getDoubleExtra("price", POSITIVE_INFINITY)
        val description = intent.getStringExtra("description") ?: ""

        addToCartButton.setOnClickListener {
            canProductBeAdded = true
            mDatabaseReference.child(product.id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && canProductBeAdded) {
                        var number = snapshot.value.toString().toInt()
                        mDatabaseReference.child(product.id).setValue(number + 1)
                    } else if (!snapshot.exists() && canProductBeAdded) {
                        mDatabaseReference.child(product.id).setValue(1)
                    }
                    if (canProductBeAdded) {
                        Toast.makeText(
                            applicationContext,
                            R.string.productAddedToCartToast,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    canProductBeAdded = false
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        loadImageAndAvailabilityBanner(product, productStockSync)
        product_name.text = title
        productPrice.text = "\u20B9" + price
        productDescription.text = description
//todo check availability by checking stock and lock
        // todo do same in settings by adding show out of stock and opposite switch
        availability.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Hey $title is in stock!")
                .setPositiveButton("OK") { p0, p1 ->
                }
                .create()
                .show()
        }
    }

    private fun loadImageAndAvailabilityBanner(
        product: Product,
        productStockSync: ProductStockSync
    ) {
        Picasso.get().load(intent.getStringExtra("imageUrl")).into(photo)

        if (productStockSync.stock == 0) {
            d(
                "ProductDetails",
                "loadImageAndAvailabilityBanner-Not available${product.id}"
            )
            photo.alpha = 0.5F
            product_details_notAvailableTextView.text = "Not Available"
            product_details_notAvailableTextView.visibility = View.VISIBLE
        } else if (!isProductAvailableConditions(productStockSync)) {
            d(
                "ProductDetails",
                "loadImageAndAvailabilityBanner-Coming soon${product.id}"
            )
            photo.alpha = 0.75F
            product_details_notAvailableTextView.text = "Coming Soon"
            product_details_notAvailableTextView.visibility = View.VISIBLE
        } else {
            d(
                "ProductDetails",
                "loadImageAndAvailabilityBanner-Available${product.id}"
            )
            photo.alpha = 1F
            product_details_notAvailableTextView.text = ""
            product_details_notAvailableTextView.visibility = View.INVISIBLE
        }
        d(
            "ProductDetails",
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_toolbar_user_cart_actionCart) {
            d("anurag", "going to cart")
            startActivity(Intent(this, UserCartActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_user_cart, menu)
        return true
    }
}