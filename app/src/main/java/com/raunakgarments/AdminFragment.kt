package com.raunakgarments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment(productActivityNew: AdminProductActivityNew) : Fragment() {


    lateinit var title: String
    var price: Double = 0.0
    var stock = 0
    var link = "https://firebasestorage.googleapis.com/v0/b/raunak-garments.appspot.com/o/productImages%2F1285051925?alt=media&token=85c30b32-3f21-42e4-8d08-d927f1e76d7f"
    var description: String = ""
    lateinit var ref: String
    lateinit var firebaseUtil: FirebaseUtil
    lateinit var mFirebaseDatebase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    private val PICTURE_RESULT = 42
    lateinit var contextView: View
    var context = productActivityNew

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(
            R.layout.fragment_admin, container, false
        )
        var uploadImageButton: Button =
            view.findViewById<Button>(R.id.uploadImageButtonAdmin)
        uploadImageButton.setOnClickListener {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT)
        }
        this.contextView = view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seeAllProductsButtonAdmin.setOnClickListener {
            context.supportFragmentManager.beginTransaction()
                .replace(R.id.product_main_fragment_admin, AdminProductFragmentNew(context)).commit()
        }
        submitButtonAdmin.setOnClickListener {
            if (productTitleAdmin.text.toString() == "" || productPriceAdmin.text.toString() == "") {
                clean()
            } else {
                this.title = productTitleAdmin.text.toString()
                this.price = productPriceAdmin.text.toString().toDouble()
                this.stock = productStockAdmin.text.toString().toInt()
                if (productDescriptionAdmin.text.toString() != "") {
                    this.description = productDescriptionAdmin.text.toString()
                }
                if (productImageLinkAdmin.text.toString() != "") {
                    this.link = productImageLinkAdmin.text.toString()
                }

                this.ref = "products"
                this.firebaseUtil = FirebaseUtil()
                firebaseUtil.openFbReference(ref)
                this.mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
                this.mDatabaseReference = firebaseUtil.mDatabaseReference
                saveDeal()
                Toast.makeText(activity, "Product Added", Toast.LENGTH_LONG).show()
            }
        }
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
                        productImageLinkAdmin.setText(url)
                        Picasso.get().load(url).into(uploadedImagePreviewAdmin)
                    }
                }
            }
        }
    }

    private fun saveDeal() {
        var product = Product()
        product.populate(title, price, link, description)
        product.stock = this.stock
        val pushReferenceKey = mDatabaseReference.push().key
        if (pushReferenceKey != null) {
            product.id = pushReferenceKey
            mDatabaseReference.child(pushReferenceKey).setValue(product)
        }
        clean()
    }

    private fun clean() {
        productTitleAdmin.setText("")
        productPriceAdmin.setText("")
        productDescriptionAdmin.setText("")
        productImageLinkAdmin.setText("")
        productStockAdmin.setText("")
        productTitleAdmin.requestFocus()
        Picasso.get().load("empty view").into(uploadedImagePreviewAdmin)
    }
}