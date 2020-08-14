package com.raunakgarments

import android.content.Context
import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso

class AdminProductSearchAdapterNew : RecyclerView.Adapter<AdminProductSearchAdapterNew.DealViewHolder>() {

    var products: MutableList<Product> = ArrayList()
    private lateinit var mFirebaseDatebase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private lateinit var listener: (Product) -> Unit
    private lateinit var context: Context

    fun populate(ref: String, productIds: MutableList<String>, context: Context) {
        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        firebaseUtil.openFbReference(ref)
        for (productId in productIds) {
            d("ref","$ref/$productId")
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
//        mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
//        mDatabaseReference = firebaseUtil.mDatabaseReference
//        childEventListener = object : ChildEventListener {
//            override fun onCancelled(error: DatabaseError) {}
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onChildRemoved(snapshot: DataSnapshot) {}
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                d("anurag","I over here")
//                var td = snapshot.getValue(Product::class.java)
//                if (td != null) {
//                    td.id = snapshot.key.toString()
//                    d(td.price.toString(),"lksd")
//                    products.add(td)
//                    d("anurag", "${td.price.toString()}")
//                    notifyItemInserted(products.size-1)
//                }
//            }
//        }
//        mDatabaseReference.addChildEventListener(childEventListener)
        d("anurag","${products.size}")
        this.context = context
        d("anurag","I'm populating ended")

    }

    public class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.title)
        var image: ImageView = itemView.findViewById(R.id.photo)
        var price: TextView = itemView.findViewById(R.id.price)
    }

    private fun rvItemSegue(product: Product) {
        d("anurag","I'm segueing")
        var description = ""
        try { description = product.description } finally {}
        product.description = description

        var intent = Intent(context ,AdminProductDetails::class.java)
        intent.putExtra("product", Gson().toJson(product))
        context.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        var context = parent.context
        var itemView = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false)
        return DealViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        d("anurag","${products.size}")
        return products.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        var product = products[position]
        holder.tvTitle.setText(product.title)
        holder.price.text = "\u20b9" + product.price
        Picasso.get().load(product.photoUrl).into(holder.image)
        holder.itemView.setOnClickListener { rvItemSegue(product) }
    }
}