package com.raunakgarments.admin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import java.io.File


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
    private val STORAGE_PERMISSION_CODE = 1
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

                d("AdminFragment", "onActivityResult height - $imageUri")

//todo
                var imageOrientation = 0
                try {
                    if(File(getRealPathFromURI(imageUri)).exists()) {
                        imageOrientation = getOrientation(context, imageUri)
                    } else {
                        d(
                            "AdminFragment",
                            "onActivityResult orientation - imageuri does not exist}"
                        )
                    }
                } catch (e: Exception) {
                    d("AdminFragment", "onActivityResult orientation - GetRealpath error}")
                }
                d("AdminFragment", "onActivityResult orientation - ${imageOrientation}")

                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(
                    context.contentResolver.openInputStream(imageUri),
                    null,
                    options
                )
                d("AdminFragment", "onActivityResult height - ${options.outHeight}")
                d("AdminFragment", "onActivityResult width - ${options.outWidth}")

                val imageStream = context.contentResolver.openInputStream(
                    imageUri
                )
                var bitmap = BitmapFactory.decodeStream(imageStream)

                if(imageOrientation != 0 ) {
                    var matrix = Matrix()
                    matrix.postRotate(imageOrientation.toFloat())
                    bitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                }

                val imageWidth = 720
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    imageWidth,
                    options.outHeight / (options.outWidth / imageWidth),
                    false
                )
                val baos = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
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

    private fun getOrientation(context: Context, photoUri: Uri): Int {
        var cursor: Cursor? = context.getContentResolver()
            .query(photoUri, arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null)
        if (cursor!!.count != 1) {
            cursor.close()
            return -1
        }
        cursor.moveToFirst()
        val orientation = cursor.getInt(0)
        cursor.close()
        cursor = null
        return orientation
    }

    private fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "You have already granted the permission", Toast.LENGTH_LONG)
            d("AdminFragment", "checkPermissions - permission already granted")
        } else {
            d("AdminFragment", "checkPermissions - permission already not granted")
            requestStoragePermission()

        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            d("AdminFragment", "requestStoragePermission - showing popup")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Permission needed")
            builder.setMessage("This permission is needed because of this and that")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("OK") { dialogInterface, which ->
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            }
            builder.setNegativeButton("cancel") { dialogInterface, which ->
                dialogInterface.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        } else {
            d("AdminFragment", "requestStoragePermission - not showing popup")
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT)
                    d("AdminFragment", "onRequestPermissionsResult - permission granted")
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT)
                    d("AdminFragment", "onRequestPermissionsResult - permission denied")
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        val cursor: Cursor = context.contentResolver.query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
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