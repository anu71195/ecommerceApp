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
import com.raunakgarments.global.UserCartSingletonClass
import com.raunakgarments.helper.ProductStockSyncHelper
import com.raunakgarments.model.Product
import com.raunakgarments.model.ProductStockSync
import com.raunakgarments.model.ProductStockSyncAdminLock
import com.raunakgarments.model.Profile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin_products_edit.*
import kotlinx.android.synthetic.main.activity_admin_products_edit_content_scrolling.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
        getProductLockClickListener(product)

    }

    private fun getProductLockClickListener(product: Product) {
        activity_admin_products_edit_content_scrolling_getProduct.setOnClickListener {
            var productStockSyncFirebaseUtil = FirebaseUtil()
            productStockSyncFirebaseUtil.openFbReference("productStockSync")

            var productStockSyncAdminFirebaseUtil = FirebaseUtil()
            productStockSyncAdminFirebaseUtil.openFbReference("productStockSyncAdminLock")

            productStockSyncFirebaseUtil.mDatabaseReference.child(product.id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var productStockSync =
                                snapshot.getValue(ProductStockSync::class.java)
                            if (productStockSync != null && checkConditionsForLock(productStockSync)) {
                                //lock accessibile

                                getLockAndPopulateProductStockSyncSnapshot(
                                    productStockSync,
                                    snapshot
                                )
                            } else {
                                //lock not accessible
                                Toast.makeText(
                                    applicationContext,
                                    "Lock not available - try again after sometime",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            d(
                                "AdminProductsEdit",
                                "getProductLockListener :- snapshot does not exists"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })
        }
    }

    private fun getLockAndPopulateProductStockSyncSnapshot(
        productStockSync: ProductStockSync,
        snapshot: DataSnapshot
    ) {

        val totalBoughtItems = calculateTotalBoughtItems(productStockSync)
        val istTime = getIstTime()

        populateProductStockSyncSnapshot(productStockSync, totalBoughtItems, istTime, snapshot)
    }

    private fun populateProductStockSyncSnapshot(
        productStockSync: ProductStockSync,
        totalBoughtItems: Int,
        istTime: SimpleDateFormat,
        snapshot: DataSnapshot
    ) {
        productStockSync.locked = FirebaseAuth.getInstance().uid.toString()
        productStockSync.stock = productStockSync.stock - totalBoughtItems
        productStockSync.boughtTicket = java.util.HashMap<String, Int>()
        productStockSync.dateStamp = istTime.format(Date())
        productStockSync.timeStamp = ((Date().time) / 1000).toString()
        productStockSync.adminLock = true

        if (productStockSync != null) {
            ProductStockSyncHelper().setValueInChild(
                snapshot.key.toString(),
                productStockSync
            )
        }

        updateAcquiredTimeStampAndSetTimeDelayCheckLockedUser()

    }

    private fun updateAcquiredTimeStampAndSetTimeDelayCheckLockedUser() {
        UserCartSingletonClass.productLockAcquiredTimeStamp =
            (((Date().time) / 1000))


        Handler().postDelayed({
            getProfileAndCheckForLockUser(FirebaseAuth.getInstance().uid.toString())
        }, 2000)

    }

    private fun getProfileAndCheckForLockUser(userID: String) {
        checkForLockUser(userID)
    }

    private fun checkForLockUser(userID: String) {
        var productStockSyncFirebaseUtil = FirebaseUtil()
        productStockSyncFirebaseUtil.openFbReference("productStockSync")
        productStockSyncFirebaseUtil.mDatabaseReference.child(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var productStockSync =
                            snapshot.getValue(ProductStockSync::class.java)
                        if (productStockSync != null) {
                            if (productStockSync.locked == FirebaseAuth.getInstance().uid.toString() && productStockSync.adminLock) {
                                adminLockRetrievedProcessing()
                                d(
                                    "AdminProductsEdit",
                                    "checkForLockUser:-product lock is available"
                                )
                            } else {
                                d(
                                    "AdminProductsEdit",
                                    "checkForLockUser:-product lock is not available"
                                )
                            }
                        }
                    } else {
                        d("AdminProductsEdit", "checkForLockUser:-snapshot does not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun adminLockRetrievedProcessing() {

        refreshTextFields()

        activity_admin_products_edit_content_scrolling_productTitleAdmin.isEnabled = true

        activity_admin_products_edit_content_scrolling_productPriceAdmin.isEnabled = true

        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.isEnabled = true

        activity_admin_products_edit_content_scrolling_productDescriptionAdmin.isEnabled = true

        activity_admin_products_edit_content_scrolling_uploadImageButtonAdmin.isEnabled = true

        activity_admin_products_edit_content_scrolling_productStockAdmin.isEnabled = true

        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.isEnabled = true
        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.background =
            ContextCompat.getDrawable(
                this@AdminProductsEdit,
                R.drawable.button_red_green_color_selector
            )

        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.isEnabled = true
        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.background =
            ContextCompat.getDrawable(
                this@AdminProductsEdit,
                R.drawable.button_red_green_color_selector
            )
    }

    private fun refreshTextFields() {
        var productId = this.productId
        var productFirebaseUtil = FirebaseUtil()
        productFirebaseUtil.openFbReference("products")

        productFirebaseUtil.mDatabaseReference.child(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var product = snapshot.getValue(Product::class.java)
                        if (product != null) {
                            activity_admin_products_edit_content_scrolling_productTitleAdmin.setText(product.title)

                            activity_admin_products_edit_content_scrolling_productPriceAdmin.setText(product.price.toString())

                            activity_admin_products_edit_content_scrolling_productImageLinkAdmin.setText(product.photoUrl)

                            activity_admin_products_edit_content_scrolling_productDescriptionAdmin.setText(product.description)

                            Picasso.get().load(product.photoUrl)
                                .into(activity_admin_products_edit_content_scrolling_uploadedImagePreviewAdmin)
                            productId = product.id
                            tagArray = product.tagArray

                        } else {
                            d("AdminProductsEdit", "refreshTextFields - product is null")
                        }
                    } else {
                        d("AdminProductsEdit", "refreshTextFields - snapshot does not exist")
                    }

                }

                override fun onCancelled(error: DatabaseError) {}

            })

        var productStockFirebaseUtil = FirebaseUtil()
        productStockFirebaseUtil.openFbReference("productStockSync")
        productStockFirebaseUtil.mDatabaseReference.child(productId)
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


    }

    private fun getIstTime(): SimpleDateFormat {
        var istTime = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        istTime.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return istTime
    }

    private fun calculateTotalBoughtItems(productStockSync: ProductStockSync): Int {
        var totalBoughtItems = 0
        if (productStockSync != null) {
            for (boughtItems in productStockSync.boughtTicket) {
                totalBoughtItems += boughtItems.value
            }
        }
        return totalBoughtItems
    }

    private fun checkConditionsForLock(productStockSync: ProductStockSync): Boolean {
        return ((productStockSync.locked == "-1" || checkTimeStampStatus(
            productStockSync.timeStamp
        ) || productStockSync.locked == FirebaseAuth.getInstance().uid.toString()))
    }

    // time stamp product stock sync delay = 600 seconds
    private fun checkTimeStampStatus(timeStamp: String): Boolean {
        return ((((Date().time) / 1000) - timeStamp.toLong()) > 600)
    }

    private fun releaseLockButtonClickListener(product: Product) {
        activity_admin_products_edit_content_scrolling_releaseLocks.setOnClickListener {
            var productStockSyncAdminLockFirebaseUtil = FirebaseUtil()
            productStockSyncAdminLockFirebaseUtil.openFbReference(getString(R.string.database_product_stock_sync_admin_lock))

            var productStockSyncFirebaseUtil = FirebaseUtil()
            productStockSyncFirebaseUtil.openFbReference("productStockSync")

            productStockSyncFirebaseUtil.mDatabaseReference.child(product.id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var productStockSync =
                                snapshot.getValue(ProductStockSync::class.java)
                            if (productStockSync != null) {
                                if (productStockSync.locked ==
                                    FirebaseAuth.getInstance().uid.toString() && productStockSync.adminLock
                                ) {
                                    productStockSync.locked = "-1"
                                    productStockSync.adminLock = false
                                    ProductStockSyncHelper().setValueInChild(
                                        product.id,
                                        productStockSync
                                    )

                                    activity_admin_products_edit_content_scrolling_productTitleAdmin.isEnabled =
                                        false

                                    activity_admin_products_edit_content_scrolling_productPriceAdmin.isEnabled =
                                        false

                                    activity_admin_products_edit_content_scrolling_productImageLinkAdmin.isEnabled =
                                        false

                                    activity_admin_products_edit_content_scrolling_productDescriptionAdmin.isEnabled =
                                        false

                                    activity_admin_products_edit_content_scrolling_uploadImageButtonAdmin.isEnabled =
                                        false

                                    activity_admin_products_edit_content_scrolling_productStockAdmin.isEnabled =
                                        false

                                    activity_admin_products_edit_content_scrolling_UpdateProductAdmin.isEnabled =
                                        false
                                    activity_admin_products_edit_content_scrolling_UpdateProductAdmin.background =
                                        ContextCompat.getDrawable(
                                            this@AdminProductsEdit,
                                            R.drawable.rounded_corners_unselected_red
                                        )

                                    activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.isEnabled =
                                        false
                                    activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.background =
                                        ContextCompat.getDrawable(
                                            this@AdminProductsEdit,
                                            R.drawable.rounded_corners_unselected_red
                                        )
                                }
                            }
                        } else {
                            d(
                                "AdminProductsEdit",
                                "releaseLockButtonClickListener:-snapshot does not exist"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

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
                                    500
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
        Handler().postDelayed({ ifLockStillExistsUnlockProduct(product) }, 1500)

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

        this.productId = product.id

        activity_admin_products_edit_content_scrolling_productStockAdmin.isEnabled = false

        activity_admin_products_edit_content_scrolling_releaseLocks.isEnabled = false
        activity_admin_products_edit_content_scrolling_releaseLocks.background =
            ContextCompat.getDrawable(this, R.drawable.rounded_corners_unselected_red)

        activity_admin_products_edit_content_scrolling_getProduct.isEnabled = false
        activity_admin_products_edit_content_scrolling_getProduct.background =
            ContextCompat.getDrawable(this, R.drawable.rounded_corners_unselected_red)

        Handler().postDelayed({ checkAndSetReleaseLockEnability(product) }, 1500)

        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.isEnabled = false
        activity_admin_products_edit_content_scrolling_UpdateProductAdmin.background =
            ContextCompat.getDrawable(this, R.drawable.rounded_corners_unselected_red)

        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.isEnabled = false
        activity_admin_products_edit_content_scrolling_DeleteButtonAdmin.background =
            ContextCompat.getDrawable(this, R.drawable.rounded_corners_unselected_red)

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
        activity_admin_products_edit_content_scrolling_productTitleAdmin.isEnabled = false

        activity_admin_products_edit_content_scrolling_productPriceAdmin.setText(product.price.toString())
        activity_admin_products_edit_content_scrolling_productPriceAdmin.isEnabled = false

        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.setText(product.photoUrl)
        activity_admin_products_edit_content_scrolling_productImageLinkAdmin.isEnabled = false

        activity_admin_products_edit_content_scrolling_productDescriptionAdmin.setText(product.description)
        activity_admin_products_edit_content_scrolling_productDescriptionAdmin.isEnabled = false

        activity_admin_products_edit_content_scrolling_uploadImageButtonAdmin.isEnabled = false

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
                            activity_admin_products_edit_content_scrolling_getProduct.isEnabled =
                                true
                            activity_admin_products_edit_content_scrolling_getProduct.background =
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
                            activity_admin_products_edit_content_scrolling_getProduct.isEnabled =
                                false
                            activity_admin_products_edit_content_scrolling_getProduct.background =
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

                        activity_admin_products_edit_content_scrolling_getProduct.isEnabled = false
                        activity_admin_products_edit_content_scrolling_getProduct.background =
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
//        this.startActivity(intent)
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
//todo handle the progress bar