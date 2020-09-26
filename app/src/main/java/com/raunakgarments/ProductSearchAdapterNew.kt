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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso
import java.lang.Exception

class ProductSearchAdapterNew : RecyclerView.Adapter<ProductSearchAdapterNew.DealViewHolder>() {

    var products: MutableList<Product> = ArrayList()
    private lateinit var mFirebaseDatebase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private lateinit var listener: (Product) -> Unit
    private lateinit var context: Context
    private lateinit var fragment_products_new_progressBar: ProgressBar

    fun populate(
        ref: String,
        productIds: MutableList<String>,
        context: Context,
        fragment_products_new_progressBar: ProgressBar
    ) {
        this.fragment_products_new_progressBar = fragment_products_new_progressBar

        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference(ref)

        for (productId in productIds) {
            d("ref", "$ref/$productId")
            firebaseUtil.mDatabaseReference.child(productId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
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
    }

    private fun rvItemSegue(product: Product) {
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

    private fun checkAndResetProgressBarVisibility(position: Int) {
        if (position == minOf(products.size, 4) - 1) {
            fragment_products_new_progressBar.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {

        var product = products[position]
        holder.tvTitle.setText(product.title)
        holder.price.text = "\u20b9" + product.price
        Picasso.get().load(product.photoUrl).into(holder.image, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
                checkAndResetProgressBarVisibility(position)
            }

            override fun onError(e: Exception?) {
                d("productadapternew", "onbindviewholder - image not loaded")
            }
        })
        holder.itemView.setOnClickListener { rvItemSegue(product) }
    }
}