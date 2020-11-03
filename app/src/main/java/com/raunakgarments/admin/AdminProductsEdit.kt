package com.raunakgarments.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.R
import com.raunakgarments.helper.ProductStockSyncHelper
import com.raunakgarments.model.Product
import com.raunakgarments.model.ProductStockSync
import com.raunakgarments.model.ProductStockSyncAdminLock
import com.raunakgarments.model.Profile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin_products_edit.*
import kotlinx.android.synthetic.main.activity_admin_products_edit_content_scrolling.*

class AdminProductsEdit : AppCompatActivity() {

    lateinit var productId: String
    var tagArray: HashMap<String, Int> = HashMap<String, Int>()
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

        populateTextFields(product)
        getLocksButtonClickListener(product)
        uploadImageButtonClickListener()
        editButtonClickListener(product)
        deleteButtonClickListener()
        releaseLockButtonClickListener(product)

    }

    private fun releaseLockButtonClickListener(product: Product) {
        activity_admin_products_edit_content_scrolling_releaseLocks.setOnClickListener {
            var productStockSyncAdminLockFirebaseUtil = FirebaseUtil()
            productStockSyncAdminLockFirebaseUtil.openFbReference(getString(R.string.database_product_stock_sync_admin_lock))

            productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(product.id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var productStockSyncAdminLock =
                                snapshot.getValue(ProductStockSyncAdminLock()::class.java)
                            if (productStockSyncAdminLock != null) {
                                productStockSyncAdminLock.adminId = ""
                                productStockSyncAdminLock.adminLock = false
                                productStockSyncAdminLock.adminName = ""
                                productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(
                                    product.id
                                )
                                    .setValue(productStockSyncAdminLock)
                                Handler().postDelayed(
                                    { checkAndSetReleaseLockEnability(product) },
                                    1500
                                )

                            } else {
                                d(
                                    "AdminProductsEdit",
                                    "checkAndSetReleaseLockENability :- product does not exits"
                                )
                            }
                        } else {
                            d(
                                "AdminProductsEdit",
                                "checkAndSetReleaseLockENability :- snapshot does not exits"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    private fun getLocksButtonClickListener(product: Product) {

        activity_admin_products_edit_progressBar.visibility = View.VISIBLE

        activity_admin_products_edit_content_scrolling_getLocks.setOnClickListener {

            var userProfileFirebaseUtil = FirebaseUtil()
            userProfileFirebaseUtil.openFbReference("userProfile/")

            userProfileFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var userProfile = snapshot.getValue(Profile::class.java)
                            d(
                                "AdminProductsEdit",
                                "getLocksButtonClickListener-${Gson().toJson(userProfile)}"
                            )
                            if (userProfile != null) {
                                checkAndSetProductSyncAdminLock(product, userProfile)
                            }
                        } else {
                            d(
                                "AdminProductsEdit",
                                "getLocksButtonClickListener-snapshot does not exist"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

            //todo set timer to check if still the productstocksync is available for admin
            //todo then try to get locks in productstocksync
            //todo if adminLock is true then user will not get lock unless he already has
            //todo release productstocksynclock and adminLock when done\
            //todo when updating the stock make sure the lock is the users
            //todo release adminLock when done even when productstocksynclock is not retrieved
            //todo for user if adminlock is true do not get locks else BAU but anywhere where productstocksync is getting udpated check the adminlock
        }
    }

    //checks if admin can take adminlock
    private fun checkAndSetProductSyncAdminLock(product: Product, userProfile: Profile) {
        var productStockSyncAdminLockFirebaseUtil = FirebaseUtil()
        productStockSyncAdminLockFirebaseUtil.openFbReference(getString(R.string.database_product_stock_sync_admin_lock))

        productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(product.id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var productStockSyncAdminLock =
                            snapshot.getValue(ProductStockSyncAdminLock()::class.java)
                        if (productStockSyncAdminLock != null && productStockSyncAdminLock.adminLock && productStockSyncAdminLock.adminId != FirebaseAuth.getInstance().uid) {
                            showAdminLockNotAvailablePopup(productStockSyncAdminLock)
                        } else {
                            setProductSyncAdminLock(
                                product,
                                userProfile,
                                productStockSyncAdminLockFirebaseUtil
                            )
                            checkIfAdminLockStillExists(product)
                        }

                    } else {
                        d(
                            "AdminProductsEdit",
                            "checkAndSetProductSyncAdminLock-snapshot does not exist"
                        )
                        setProductSyncAdminLock(
                            product,
                            userProfile,
                            productStockSyncAdminLockFirebaseUtil
                        )
                        checkIfAdminLockStillExists(product)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun checkIfAdminLockStillExists(product: Product) {
        Handler().postDelayed({ ifLockStillExistsUnlockProduct(product) }, 5000)

    }

    private fun ifLockStillExistsUnlockProduct(product: Product) {
        var productStockSyncAdminLockFirebaseUtil = FirebaseUtil()
        productStockSyncAdminLockFirebaseUtil.openFbReference(getString(R.string.database_product_stock_sync_admin_lock))

        productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(product.id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var productStockSyncAdminLock =
                            snapshot.getValue(ProductStockSyncAdminLock()::class.java)

                        if (productStockSyncAdminLock != null && productStockSyncAdminLock.adminLock && productStockSyncAdminLock.adminId == FirebaseAuth.getInstance().uid) {
                            d("AdminProductsEdit", "ifLockStillExistsUnlockProduct :- got lock")
                            getProductLock(product)
                        } else {
                            d(
                                "AdminProductsEdit",
                                "ifLockStillExistsUnlockProduct :- Did not get lock"
                            )
                        }
                    } else {
                        d(
                            "AdminProductsEdit",
                            "ifLockStillExistsUnlockProduct :- Snapshot does not exist"
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun getProductLock(product: Product) {
        Handler().postDelayed({ checkAndSetReleaseLockEnability(product) }, 1500)

        d(
            "adminproductsedit",
            "getproductlock-if you did not get lock don't worry you will get next lock"
        )
        //todo add releaselock button from and add productlock button
        //todo get the product lock here
    }

    private fun showAdminLockNotAvailablePopup(productStockSyncAdminLock: ProductStockSyncAdminLock) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Admin Lock Not Available")
        builder.setMessage("Currently Lock is with ${productStockSyncAdminLock.adminName} with id ${productStockSyncAdminLock.adminId}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setNeutralButton("OK") { dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setProductSyncAdminLock(
        product: Product,
        userProfile: Profile,
        productStockSyncAdminLockFirebaseUtil: FirebaseUtil
    ) {
        var productStockSyncAdminLock = ProductStockSyncAdminLock()
        productStockSyncAdminLock.productId = product.id
        productStockSyncAdminLock.adminLock = true
        productStockSyncAdminLock.adminId = FirebaseAuth.getInstance().uid.toString()
        productStockSyncAdminLock.adminName = userProfile.userName

        productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(product.id)
            .setValue(productStockSyncAdminLock)
    }

    private fun populateTextFields(product: Product) {

        activity_admin_products_edit_content_scrolling_productStockAdmin.isEnabled = false

        activity_admin_products_edit_content_scrolling_releaseLocks.isEnabled = false
        activity_admin_products_edit_content_scrolling_releaseLocks.background =
            ContextCompat.getDrawable(this, R.drawable.rounded_corners_unselected_red)

        Handler().postDelayed({ checkAndSetReleaseLockEnability(product) }, 1500)

        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.isEnabled = false
        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.background =
            ContextCompat.getDrawable(this, R.drawable.rounded_corners_unselected_red)

        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.isEnabled = false
        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.background =
            ContextCompat.getDrawable(this, R.drawable.rounded_corners_unselected_red)

        //todo refresh the contents in the textfields when enabling them true after getting locks

        var productStockFirebaseUtil = FirebaseUtil()
        productStockFirebaseUtil.openFbReference("productStockSync")
        productStockFirebaseUtil.mDatabaseReference.child(product.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var productStock = snapshot.getValue(ProductStockSync::class.java)
                    if (productStock != null) {
                        activity_admin_products_edit_content_scrolling_productStockAdmin.setText(
                            productStock.stock.toString()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        activity_admin_products_edit_content_scrolling_productTitleAdmin.setText(product.title)
        activity_admin_products_edit_content_scrolling_productPriceAdmin.setText(product.price.toString())
        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.setText(product.photoUrl)
        activity_admin_products_edit_content_scrolling_productDescriptionAdmin.setText(product.description)
        Picasso.get().load(product.photoUrl)
            .into(activity_admin_products_edit_content_scrolling_uploadedImagePreviewAdmin)
        productId = product.id
        tagArray = product.tagArray
    }


    private fun checkAndSetReleaseLockEnability(product: Product) {
        var productStockSyncAdminLockFirebaseUtil = FirebaseUtil()
        productStockSyncAdminLockFirebaseUtil.openFbReference(getString(R.string.database_product_stock_sync_admin_lock))

        productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(product.id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var productStockSyncAdminLock =
                            snapshot.getValue(ProductStockSyncAdminLock()::class.java)
                        if (productStockSyncAdminLock != null && productStockSyncAdminLock.adminLock && productStockSyncAdminLock.adminId == FirebaseAuth.getInstance().uid) {
                            activity_admin_products_edit_content_scrolling_releaseLocks.isEnabled =
                                true
                            activity_admin_products_edit_content_scrolling_releaseLocks.background =
                                ContextCompat.getDrawable(
                                    this@AdminProductsEdit,
                                    R.drawable.button_red_green_color_selector
                                )
                        } else {
                            activity_admin_products_edit_content_scrolling_releaseLocks.isEnabled =
                                false
                            activity_admin_products_edit_content_scrolling_releaseLocks.background =
                                ContextCompat.getDrawable(
                                    this@AdminProductsEdit,
                                    R.drawable.rounded_corners_unselected_red
                                )
                        }
                    } else {
                        activity_admin_products_edit_content_scrolling_releaseLocks.isEnabled =
                            false
                        activity_admin_products_edit_content_scrolling_releaseLocks.background =
                            ContextCompat.getDrawable(
                                this@AdminProductsEdit,
                                R.drawable.rounded_corners_unselected_red
                            )
                        d(
                            "AdminProductsEdit",
                            "checkAndSetReleaseLockENability :- snapshot does not exits"
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun uploadImageButtonClickListener() {
        var uploadImageButton: Button =
            findViewById<Button>(R.id.activity_admin_products_edit_content_scrolling_uploadImageButtonAdmin)
        uploadImageButton.setOnClickListener {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT)
        }
    }

    private fun deleteButtonClickListener() {
        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
                var tagFirebaseUtil = FirebaseUtil()
                for (tag in tagArray) {
                    tagFirebaseUtil.openFbReference("tags/${tag.key}")
                    tagFirebaseUtil.mDatabaseReference.child(productId).removeValue()
                }
                ProductStockSyncHelper().removeValueFromChild(productId)
                mDatabaseReference.child(productId).removeValue()
                var intent = Intent(this, AdminProductActivityNew::class.java)
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
    }

    private fun editButtonClickListener(product: Product) {
        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                positiveClickListenerOverPopUpOnUpdateProduct(product)
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
    }

    private fun positiveClickListenerOverPopUpOnUpdateProduct(product: Product) {
        var productStockSyncStock = 0
        if (activity_admin_products_edit_content_scrolling_productStockAdmin.text.toString() != "") {
            productStockSyncStock =
                activity_admin_products_edit_content_scrolling_productStockAdmin.text.toString()
                    .toInt()
        } else {
            productStockSyncStock = 0
        }
//todo admin lock
        var productStockFirebaseUtil = FirebaseUtil()
        productStockFirebaseUtil.openFbReference("productStockSync")
        productStockFirebaseUtil.mDatabaseReference.child(product.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var productStockSync = snapshot.getValue(ProductStockSync::class.java)
                    if (productStockSync != null) {
                        productStockSync.stock = productStockSyncStock
                        ProductStockSyncHelper().setValueInChild(
                            snapshot.key.toString(),
                            productStockSync
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
        product.title =
            activity_admin_products_edit_content_scrolling_productTitleAdmin.text.toString()
        product.price =
            activity_admin_products_edit_content_scrolling_productPriceAdmin.text.toString()
                .toDouble()

        if (activity_admin_products_edit_content_scrolling_productDescriptionAdmin.text.toString() != "") {
            product.description =
                activity_admin_products_edit_content_scrolling_productDescriptionAdmin.text.toString()
        } else {
            product.description = ""
        }
        if (activity_admin_products_edit_content_scrolling_productImageLinkAdmin.text.toString() != "") {
            product.photoUrl =
                activity_admin_products_edit_content_scrolling_productImageLinkAdmin.text.toString()
        } else {
            product.photoUrl =
                "https://firebasestorage.googleapis.com/v0/b/raunak-garments.appspot.com/o/productImages%2F1285051925?alt=media&token=85c30b32-3f21-42e4-8d08-d927f1e76d7f"
        }
        var tagFirebaseUtil = FirebaseUtil()

        //removing old tags
        for (tag in tagArray) {
            tagFirebaseUtil.openFbReference("tags/${tag.key}")
            tagFirebaseUtil.mDatabaseReference.child(productId).removeValue()
        }

        //adding new tags
        var tagList = product.title.split(" ", ",")
        val re = Regex("[^A-Za-z0-9]")
        product.tagArray = HashMap<String, Int>()
        for (tag in tagList) {
            var processedTag = re.replace(tag.toLowerCase(), "")
            d("EditTags", processedTag)
            if (processedTag != "") {
                product.tagArray[processedTag] = 1
                tagFirebaseUtil.openFbReference("tags/$processedTag")
                tagFirebaseUtil.mDatabaseReference.child(productId).setValue(1)
            }
        }

        mDatabaseReference.child(productId).setValue(product)

        var intent = Intent(this, AdminProductActivityNew::class.java)
        intent.putExtra("flow", "updateFlow")
        this.startActivity(intent)
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
                        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.setText(
                            url
                        )
                        Picasso.get().load(url).into(
                            activity_admin_products_edit_content_scrolling_uploadedImagePreviewAdmin
                        )
                    }
                }
            }
        }
    }
}