package com.raunakgarments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.widget.Button
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
import kotlinx.android.synthetic.main.fragment_admin.*
import org.jetbrains.anko.image
import org.jetbrains.anko.topPadding

class AdminProductsEdit : AppCompatActivity() {

    lateinit var productId: String
    lateinit var mDatabaseReference: DatabaseReference
    private val PICTURE_RESULT = 42

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
        productId = product.id
        d("anurag", "$productId")

        var uploadImageButton: Button =
            findViewById<Button>(R.id.activity_admin_products_edit_content_scrolling_uploadImageButtomAdmin)
        uploadImageButton.setOnClickListener {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT)
        }

        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                product.title = activity_admin_products_edit_content_scrolling_productTitleAdmin.text.toString()
                product.price = activity_admin_products_edit_content_scrolling_productPriceAdmin.text.toString().toDouble()
                if (activity_admin_products_edit_content_scrolling_productDescriptionAdmin.text.toString() != "") {
                    product.description = activity_admin_products_edit_content_scrolling_productDescriptionAdmin.text.toString()
                } else {
                    product.description = ""
                }
                if (activity_admin_products_edit_content_scrolling_productImageLinkAdmin.text.toString() != "") {
                    product.photoUrl = activity_admin_products_edit_content_scrolling_productImageLinkAdmin.text.toString()
                } else {
                    product.photoUrl = "https://firebasestorage.googleapis.com/v0/b/raunak-garments.appspot.com/o/productImages%2F1285051925?alt=media&token=85c30b32-3f21-42e4-8d08-d927f1e76d7f"
                }
                mDatabaseReference.child(productId).setValue(product)
                var intent = Intent(this ,AdminProductActivityNew::class.java)
                intent.putExtra("flow", "updateFlow")
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

        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                mDatabaseReference.child(productId).removeValue()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            var imageUri: Uri? = data?.data
            val ref =
                FirebaseUtil().mStorageRef.child("productImages/${imageUri?.lastPathSegment}")
            if (imageUri != null) {
                ref.putFile(imageUri).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        var url = it.toString()
                        d("image url", url)
                        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.setText(url)
                        Picasso.get().load(url).into(activity_admin_products_edit_content_scrolling_uploadedImagePreviewAdmin)
                    }
                }
            }
        }
    }
}