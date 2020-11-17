package com.raunakgarments.admin

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.util.FileUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raunakgarments.R
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.helper.ProductStockSyncHelper
import com.raunakgarments.model.Product
import com.raunakgarments.model.ProductStockSync
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_admin.*
import java.io.ByteArrayOutputStream


class AdminFragment(productActivityNew: AdminProductActivityNew) : Fragment() {


    lateinit var title: String
    var price: Double = 0.0
    var stock = 0
    var link =
        "https://firebasestorage.googleapis.com/v0/b/raunak-garments.appspot.com/o/productImages%2F1285051925?alt=media&token=85c30b32-3f21-42e4-8d08-d927f1e76d7f"
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
        /*todo image compression*/
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
                .replace(R.id.product_main_fragment_admin, AdminProductFragmentNew(context))
                .commit()
        }
        submitButtonAdmin.setOnClickListener {
            if (productTitleAdmin.text.toString() == "" || productPriceAdmin.text.toString() == "") {
                clean()
            } else {
                this.title = productTitleAdmin.text.toString()
                this.price = productPriceAdmin.text.toString().toDouble()

                if (productStockAdmin.text.toString() != "") {
                    this.stock = productStockAdmin.text.toString().toInt()
                }
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


//                val path = getRealPathFromURI(Uri.parse(imageUri.getPath()))
//                d("AdminFragment", "onActivityResult - ${path
//                }")
//                val exif = ExifInterface(path)
//                val rotation = exif.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL
//                )

                val imageStream = context.contentResolver.openInputStream(
                    imageUri
                )
                val bitmap = BitmapFactory.decodeStream(imageStream)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
                val data = baos.toByteArray()
                var uploadTask = ref.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
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

    fun getRealPathFromURI(uri: Uri): String? {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)

        cursor?.moveToFirst()

        val idx = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val realPath = cursor.getString(idx)

        return realPath
    }

    private fun saveDeal() {
        var tagFirebaseUtil = FirebaseUtil()
        var product = Product()
        var productStockSync = ProductStockSync()
        product.populate(title, price, link, description)
        productStockSync.stock = this.stock
        val pushReferenceKey = mDatabaseReference.push().key
        if (pushReferenceKey != null) {
            product.id = pushReferenceKey
            var tagList = product.title.split(" ", ",")
            val re = Regex("[^A-Za-z0-9]")
            for (tag in tagList) {
                var processedTag = re.replace(tag.toLowerCase(), "")
                if (processedTag != "") {
                    product.tagArray[processedTag] = 1
                    tagFirebaseUtil.openFbReference("tags/$processedTag")
                    tagFirebaseUtil.mDatabaseReference.child(pushReferenceKey).setValue(1)
                }
            }
            mDatabaseReference.child(pushReferenceKey).setValue(product)
            ProductStockSyncHelper().setValueInChild(
                pushReferenceKey,
                productStockSync
            )
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