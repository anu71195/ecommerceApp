package com.raunakgarments

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.Product
import com.raunakgarments.model.ProductStockSync
import com.raunakgarments.model.ProductStockSyncAdminLock
import com.raunakgarments.model.UserSettings
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapterNew : RecyclerView.Adapter<ProductAdapterNew.DealViewHolder>() {

    var products: MutableList<Product> = ArrayList()
    var productIdList: MutableList<String> = ArrayList()
    private lateinit var mFirebaseDatebase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var listener: (Product) -> Unit
    private lateinit var context: Context
    private lateinit var fragment_products_new_progressBar: ProgressBar
    private lateinit var rvProducts: RecyclerView
    private lateinit var productsLayoutManager: GridLayoutManager
    private var isLoadingFirstTime = true
    private lateinit var fragment_products_new_progressBarTextView: TextView
    var productStockSyncFirebaseUtil = FirebaseUtil()


    fun populate(
        ref: String,
        context: Context,
        fragment_products_new_progressBar: ProgressBar,
        rvProducts: RecyclerView,
        productsLayoutManager: GridLayoutManager,
        fragment_products_new_progressBarTextView: TextView
    ) {
        isLoadingFirstTime = true
        productStockSyncFirebaseUtil.openFbReference("productStockSync")
        this.fragment_products_new_progressBarTextView = fragment_products_new_progressBarTextView

        Handler().postDelayed({
            fragment_products_new_progressBarTextView.visibility = View.VISIBLE
        }, 10 * 1000)

        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        this.fragment_products_new_progressBar = fragment_products_new_progressBar
        this.rvProducts = rvProducts
        this.productsLayoutManager = productsLayoutManager
        firebaseUtil.openFbReference(ref)
        mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
        mDatabaseReference = firebaseUtil.mDatabaseReference

        getUserSettingsAndPopulateChildEventListener()


        d("anurag", "${products.size}")
        this.context = context
        d("anurag", "I'm populating ended")
    }

    private fun getUserSettingsAndPopulateChildEventListener() {
        var userSettingsFirebaseUtil = FirebaseUtil()
        userSettingsFirebaseUtil.openFbReference("userSettings")

        var userSettings = UserSettings()

        userSettingsFirebaseUtil.mDatabaseReference.child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userSettings = if (snapshot.exists()) {
                        snapshot.getValue(UserSettings::class.java)!!
                    } else {
                        UserSettings()
                    }
                    createAndAddProductChildEventListener(userSettings)
                }

                override fun onCancelled(error: DatabaseError) {}

            })


    }

    private fun createAndAddProductChildEventListener(userSettings: UserSettings) {
        var childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                d("anurag", "I over here")
                var td = snapshot.getValue(Product::class.java)
                if (td != null) {
                    td.id = snapshot.key.toString()
                    d(td.price.toString(), "lksd")
                    d("anurag", "${td.price.toString()}")

                    populateProductsVariableAndSync(userSettings, td)
                }
            }
        }
        mDatabaseReference.addChildEventListener(childEventListener)
    }

    private fun populateProductsVariableAndSync(userSettings: UserSettings, product: Product) {

        var productStockSyncFirebaseUtil = FirebaseUtil()
        productStockSyncFirebaseUtil.openFbReference("productStockSync")

        var productStockSyncAdminLockFirebaseUtil = FirebaseUtil()
        productStockSyncAdminLockFirebaseUtil.openFbReference("productStockSyncAdminLock")

        productStockSyncFirebaseUtil.mDatabaseReference.child(product.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var productStockSync = ProductStockSync()
                    productStockSync = if (snapshot.exists()) {
                        snapshot.getValue(ProductStockSync::class.java)!!
                    } else {
                        ProductStockSync()
                    }

                    productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(product.id)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var productStockSyncAdminLock = ProductStockSyncAdminLock()

                                productStockSyncAdminLock = if (snapshot.exists()) {
                                    snapshot.getValue(ProductStockSyncAdminLock::class.java)!!
                                } else {
                                    ProductStockSyncAdminLock()
                                }

                                addProductToProducts(
                                    productStockSync,
                                    productStockSyncAdminLock,
                                    product,
                                    userSettings
                                )
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun addProductToProducts(
        productStockSync: ProductStockSync,
        productStockSyncAdminLock: ProductStockSyncAdminLock,
        product: Product,
        userSettings: UserSettings
    ) {
        if (checkConditionsToIncludeProducts(
                productStockSync,
                productStockSyncAdminLock,
                userSettings
            )
        ) {
            if (product.id !in productIdList) {
                productIdList.add(product.id)
                products.add(product)
                notifyItemInserted(products.size - 1)
            }
        } else {
            if (product.id in productIdList) {
                products.removeAt(productIdList.indexOf(product.id))
                productIdList.remove(product.id)
                notifyDataSetChanged()
            }
        }
    }

        private fun checkConditionsToIncludeProducts(
            productStockSync: ProductStockSync,
            productStockSyncAdminLock: ProductStockSyncAdminLock,
            userSettings: UserSettings
        ): Boolean {
            if (productStockSync.stock == 0 && !userSettings.showUnavailableProducts) {
                return false
            } else if ((productStockSyncAdminLock.adminLock || productStockSync.adminLock) && !userSettings.showUnderMaintenanceProducts) {
                return false
            } else if (!isProductAvailableConditions(productStockSync) && !userSettings.showComingSoonProducts) {
                return false
            }
            return true
        }

        public class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvTitle: TextView = itemView.findViewById(R.id.title)
            var image: ImageView = itemView.findViewById(R.id.photo)
            var price: TextView = itemView.findViewById(R.id.price)
            var notAvailableTv: TextView =
                itemView.findViewById(R.id.product_row_notAvailableTextView)
        }

        private fun rvItemSegue(product: Product, productStockSync: ProductStockSync) {
            d("anurag", "I'm segueing")
            var description = ""
            try {
                description = product.description
            } finally {
            }
            var intent = Intent(context, ProductDetails::class.java)
            intent.putExtra("title", product.title)
            intent.putExtra("price", product.price)
            intent.putExtra("imageUrl", product.photoUrl)
            intent.putExtra("description", description)
            intent.putExtra("product", Gson().toJson(product))
            intent.putExtra("productStockSync", Gson().toJson(productStockSync))
            context.startActivity(intent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
            var context = parent.context
            var itemView = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false)
            return DealViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            d("anurag", "${products.size}")
            return products.size
        }

        override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
            if (fragment_products_new_progressBar.visibility == View.GONE) {
                fragment_products_new_progressBar.visibility = View.VISIBLE
            }
            var product = products[position]
            holder.tvTitle.setText(product.title)
            holder.price.text = "\u20b9" + product.price
            getProductStocksLocksDetails(holder, position, product)
            loadImageAndProgressBarVisibility(holder, position, product)
        }

        private fun getProductStocksLocksDetails(
            holder: DealViewHolder,
            position: Int,
            product: Product
        ) {
            var productStockSync: ProductStockSync

            var productStockSyncAdminLockFirebaseUtil = FirebaseUtil()
            productStockSyncAdminLockFirebaseUtil.openFbReference("productStockSyncAdminLock")

            productStockSyncFirebaseUtil.mDatabaseReference.child(product.id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            productStockSync = snapshot.getValue(ProductStockSync::class.java)!!
                            holder.itemView.setOnClickListener {
                                rvItemSegue(
                                    product,
                                    productStockSync
                                )
                            }

                            productStockSyncAdminLockFirebaseUtil.mDatabaseReference.child(product.id)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            var productStockSyncAdminLock =
                                                snapshot.getValue(ProductStockSyncAdminLock()::class.java)
                                            if (productStockSyncAdminLock != null) {
                                                productBannerText(
                                                    productStockSync,
                                                    holder,
                                                    product,
                                                    productStockSyncAdminLock
                                                )
                                            } else {
                                                productBannerTextWithoutUnderMaintenance(
                                                    productStockSync,
                                                    holder,
                                                    product
                                                )
                                                d(
                                                    "ProductAdapterNew",
                                                    "getProductStocksLocksDetails :- productStockSyncAdminLock is null"
                                                )
                                            }
                                        } else {
                                            productBannerTextWithoutUnderMaintenance(
                                                productStockSync,
                                                holder,
                                                product
                                            )
                                            d(
                                                "ProductAdapterNew",
                                                "getProductStocksLocksDetails :- snapshot does not exist"
                                            )
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}

                                })


                            d(
                                "ProductAdapterNew",
                                "getProductStocksLocksDetails-${Gson().toJson(productStockSync)}"
                            )
                        } else {
                            d(
                                "ProductAdapterNew",
                                "getProductStocksLocksDetails-snapshot does not exist"
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })
        }

        private fun productBannerTextWithoutUnderMaintenance(
            productStockSync: ProductStockSync,
            holder: DealViewHolder,
            product: Product
        ) {
            if (productStockSync.stock == 0) {
                d(
                    "ProductAdapterNew",
                    "getProductStocksLocksDetails-Not available${product.id}"
                )
                holder.image.alpha = 0.5F
                holder.notAvailableTv.text = "Not Available"
                holder.notAvailableTv.visibility = View.VISIBLE
            } else if (!isProductAvailableConditions(productStockSync)) {
                d(
                    "ProductAdapterNew",
                    "getProductStocksLocksDetails-Coming soon${product.id}"
                )
                holder.image.alpha = 0.75F
                holder.notAvailableTv.text = "Coming Soon"
                holder.notAvailableTv.visibility = View.VISIBLE
            } else {
                d(
                    "ProductAdapterNew",
                    "getProductStocksLocksDetails-Available${product.id}"
                )
                holder.image.alpha = 1F
                holder.notAvailableTv.text = ""
                holder.notAvailableTv.visibility = View.INVISIBLE
            }
        }

        private fun productBannerText(
            productStockSync: ProductStockSync,
            holder: DealViewHolder,
            product: Product,
            productStockSyncAdminLock: ProductStockSyncAdminLock
        ) {
            if (productStockSync.stock == 0) {
                d(
                    "ProductAdapterNew",
                    "getProductStocksLocksDetails-Not available${product.id}"
                )
                holder.image.alpha = 0.5F
                holder.notAvailableTv.text = "Not Available"
                holder.notAvailableTv.visibility = View.VISIBLE
            } else if (productStockSyncAdminLock.adminLock || productStockSync.adminLock) {
                d(
                    "ProductAdapterNew",
                    "getProductStocksLocksDetails-Under Maintenance${product.id}"
                )

                holder.image.alpha = 0.5F
                holder.notAvailableTv.text = "Under Maintenance"
                holder.notAvailableTv.visibility = View.VISIBLE
            } else if (!isProductAvailableConditions(productStockSync)) {
                d(
                    "ProductAdapterNew",
                    "getProductStocksLocksDetails-Coming soon${product.id}"
                )
                holder.image.alpha = 0.75F
                holder.notAvailableTv.text = "Coming Soon"
                holder.notAvailableTv.visibility = View.VISIBLE
            } else {
                d(
                    "ProductAdapterNew",
                    "getProductStocksLocksDetails-Available${product.id}"
                )
                holder.image.alpha = 1F
                holder.notAvailableTv.text = ""
                holder.notAvailableTv.visibility = View.INVISIBLE
            }
        }

        private fun isProductAvailableConditions(productStockSync: ProductStockSync): Boolean {
            return ((productStockSync.locked == "-1" || checkTimeStampStatus(
                productStockSync.timeStamp
            ) || productStockSync.locked == FirebaseAuth.getInstance().uid.toString()))
        }

        private fun checkTimeStampStatus(timeStamp: String): Boolean {
            return ((((Date().time) / 1000) - timeStamp.toLong()) > 600)
        }

        private fun loadImageAndProgressBarVisibility(
            holder: DealViewHolder,
            position: Int,
            product: Product
        ) {
            Picasso.get().load(product.photoUrl)
                .into(holder.image, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        d("productadapter", "onbindviewholder ${position}")
                        checkAndResetProgressBarVisibility(position)
                    }

                    override fun onError(e: Exception?) {
                        d("productadapternew", "onbindviewholder - image not loaded")
                    }
                })
        }

        private fun checkAndResetProgressBarVisibility(position: Int) {
            val totalItemCount = rvProducts!!.layoutManager?.itemCount
            val lastVisibleItemPosition = productsLayoutManager.findLastVisibleItemPosition()
            if ((position == (minOf(products.size, 4) - 1)) && isLoadingFirstTime) {
                fragment_products_new_progressBar.visibility = View.GONE
                isLoadingFirstTime = false
                fragment_products_new_progressBarTextView.text = " "
                Handler().postDelayed({
                    fragment_products_new_progressBarTextView.text =
                        R.string.it_is_taking_longer_than_expected_please_check_your_network_connection.toString()
                    fragment_products_new_progressBarTextView.visibility = View.GONE
                }, 10 * 1000)

            } else if (!isLoadingFirstTime) {
                fragment_products_new_progressBar.visibility = View.GONE
            }
        }
    }