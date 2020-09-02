package com.raunakgarments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.raunakgarments.repos.ProductsRepository
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_new.*
import kotlinx.android.synthetic.main.product_details.*
import org.jetbrains.anko.Android
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
                    } else if (!snapshot.exists()) {
                        mDatabaseReference.child(product.id).setValue(1)
                    }
                    canProductBeAdded = false
                }
                override fun onCancelled(error: DatabaseError) {}
            })
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