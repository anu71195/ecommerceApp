package com.raunakgarments

import android.content.Context
import android.content.Intent
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
import com.raunakgarments.model.Product
import com.raunakgarments.model.ProductStockSync
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ProductSearchAdapterNew : RecyclerView.Adapter<ProductSearchAdapterNew.DealViewHolder>() {

    var products: MutableList<Product> = ArrayList()
    private lateinit var mFirebaseDatebase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private lateinit var listener: (Product) -> Unit
    private lateinit var context: Context
    private lateinit var fragment_products_new_progressBar: ProgressBar
    private lateinit var rvProducts: RecyclerView
    private lateinit var productsLayoutManager: GridLayoutManager
    private var isLoadingFirstTime = true
    var productStockSyncFirebaseUtil = FirebaseUtil()

    fun populate(
        ref: String,
        productIds: MutableList<String>,
        context: Context,
        fragment_products_new_progressBar: ProgressBar,
        rvProducts: RecyclerView,
        productsLayoutManager: GridLayoutManager
    ) {
        isLoadingFirstTime = true
        productStockSyncFirebaseUtil.openFbReference("productStockSync")

        this.fragment_products_new_progressBar = fragment_products_new_progressBar
        this.rvProducts = rvProducts
        this.productsLayoutManager = productsLayoutManager
        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference(ref)

        for (productId in productIds) {
            d("ref", "$ref/$productId")
            firebaseUtil.mDatabaseReference.child(productId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var product = snapshot.getValue(Product::class.java)
                        if (product != null) {
                            d("productname", "${snapshot.value}")
                            products.add(product)
                            notifyItemInserted(products.size - 1)
                        }
                    }
                })
        }
//        childEventListener = object : ChildEventListener {
//            override fun onCancelled(error: DatabaseError) {}
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onChildRemoved(snapshot: DataSnapshot) {}
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                d("anurag", "I over here")
//                var td = snapshot.getValue(Product::class.java)
//                if (td != null) {
//                    td.id = snapshot.key.toString()
//                    d(td.price.toString(), "lksd")
//                    products.add(td)
//                    d("anurag", "${td.price.toString()}")
//                    notifyItemInserted(products.size - 1)
//                }
//            }
//        }
//        mDatabaseReference.addChildEventListener(childEventListener)
        d("anurag", "${products.size}")
        this.context = context
        d("anurag", "I'm populating ended")

    }

    public class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.title)
        var image: ImageView = itemView.findViewById(R.id.photo)
        var price: TextView = itemView.findViewById(R.id.price)
        var notAvailableTv: TextView = itemView.findViewById(R.id.product_row_notAvailableTextView)
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

        var product = products[position]
        holder.tvTitle.setText(product.title)
        holder.price.text = "\u20b9" + product.price

        getProductStocksLocksDetails(holder,position, product)

    }

    private fun checkTimeStampStatus(timeStamp: String): Boolean {
        return ((((Date().time) / 1000) - timeStamp.toLong()) > 600)
    }

    private fun getProductStocksLocksDetails(
        holder: DealViewHolder,
        position: Int,
        product: Product
    ) {
        var productStockSync: ProductStockSync
        productStockSyncFirebaseUtil.mDatabaseReference.child(product.id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        productStockSync = snapshot.getValue(ProductStockSync::class.java)!!
                        holder.itemView.setOnClickListener { rvItemSegue(product, productStockSync) }
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
                        d(
                            "ProductAdapterNew",
                            "getProductStocksLocksDetails-${Gson().toJson(productStockSync)}"
                        )
                        loadImageAndProgressBarVisibility(holder, position, product)
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

    private fun isProductAvailableConditions(productStockSync: ProductStockSync): Boolean {
        return ((productStockSync.locked == "-1" || checkTimeStampStatus(
            productStockSync.timeStamp
        ) || productStockSync.locked == FirebaseAuth.getInstance().uid.toString()))
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
        } else if(!isLoadingFirstTime) {
            fragment_products_new_progressBar.visibility = View.GONE

        }
    }
}