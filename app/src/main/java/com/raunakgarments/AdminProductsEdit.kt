package com.raunakgarments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin_products_edit.*
import kotlinx.android.synthetic.main.activity_admin_products_edit_content_scrolling.*
import org.jetbrains.anko.image
import org.jetbrains.anko.topPadding

class AdminProductsEdit : AppCompatActivity() {

    lateinit var dealId: String
    lateinit var mDatabaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products_edit)
        setSupportActionBar(findViewById(R.id.activity_admin_products_edit_toolbar))

        var firebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference("products")
        mDatabaseReference = firebaseUtil.mDatabaseReference

        val product =
            Gson().fromJson<Product>(intent.getStringExtra("product"), Product::class.java)
        activity_admin_products_edit_content_scrolling_productTitleAdmin.setText(product.title)
        activity_admin_products_edit_content_scrolling_productPriceAdmin.setText(product.price.toString())
        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.setText(product.photoUrl)
        activity_admin_products_edit_content_scrolling_productDescriptionAdmin.setText(product.description)
        Picasso.get().load(product.photoUrl)
            .into(activity_admin_products_edit_content_scrolling_uploadedImagePreviewAdmin)
        dealId = product.id
        d("anurag", "$dealId")

        activity_admin_products_edit_content_scrolling_AddProductAdmin.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
            }
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                Toast.makeText(
                    applicationContext,
                    "clicked cancel\n operation cancel",
                    Toast.LENGTH_LONG
                ).show()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                mDatabaseReference.child(dealId).removeValue()
                var intent = Intent(this ,AdminProductActivityNew::class.java)
                intent.putExtra("flow", "deleteFlow")
                this.startActivity(intent)
            }
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                Toast.makeText(
                    applicationContext,
                    "clicked cancel\n operation cancel",
                    Toast.LENGTH_LONG
                ).show()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG).show()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }


//        findViewById<CollapsingToolbarLayout>(R.id.activity_admin_products_edit_toolbar_layout).title = title
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}