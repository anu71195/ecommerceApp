package com.raunakgarments

import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log.d
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.raunakgarments.productdetails.ProductDetailsViewModel
import com.raunakgarments.repos.ProductsRepository
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.product_details.*
import org.jetbrains.anko.Android
import kotlin.Double.Companion.POSITIVE_INFINITY

class ProductDetails : AppCompatActivity() {
    var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
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
        val title = intent.getStringExtra("title") ?: ""
        val price = intent.getDoubleExtra("price", POSITIVE_INFINITY)
        val description = intent.getStringExtra("description") ?: ""

        addToCartButton.setOnClickListener {
            d("cart button", "clicked")
            d("userId", userId)
            mDatabaseReference.push().setValue(product.id)
        }

        Picasso.get().load(intent.getStringExtra("imageUrl")).into(photo)
        product_name.text = title
        productPrice.text = "\u20B9" + price
        productDescription.text = description

        availability.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Hey $title is in stock!")
                .setPositiveButton("OK") { p0, p1 ->
                }
                .create()
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_user_cart, menu)
        return true
    }
}