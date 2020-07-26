package com.raunakgarments

import android.content.Context
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso
import java.lang.Math.ceil

class UserCartAdapter : RecyclerView.Adapter<UserCartAdapter.DealViewHolder>() {

    var cartProduct: MutableList<Product> = ArrayList()
    private lateinit var mFirebaseDatebase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mFirebaseDatebaseProduct: FirebaseDatabase
    private lateinit var mDatabaseReferenceProduct: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
//    private lateinit var childEventListenerProduct: ChildEventListener
    private lateinit var listener: (Product) -> Unit
    private lateinit var context: Context
    var totalCost = 0.0

    fun populate(
        ref: String,
        context: Context,
        totalCostView: TextView
    ) {
        var firebaseUtil: FirebaseUtil = FirebaseUtil()
        var firebaseUtilProduct = FirebaseUtil()
        d("user address", "$ref")

        firebaseUtil.openFbReference(ref)

        mFirebaseDatebase = firebaseUtil.mFirebaseDatabase
        mDatabaseReference = firebaseUtil.mDatabaseReference
        childEventListener = object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var td = snapshot.value
                d("anurag", td.toString())
                d("anurag", "Parent")
                firebaseUtilProduct.openFbReference("products/" + snapshot.value.toString())
                mFirebaseDatebaseProduct = firebaseUtilProduct.mFirebaseDatabase
                mDatabaseReferenceProduct = firebaseUtilProduct.mDatabaseReference
                mDatabaseReferenceProduct.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}

                    override fun onDataChange(snapshot: DataSnapshot) {
                        d("anurag", "main")
                        d("anurag", "${snapshot.value}")
                        var product = snapshot.getValue(Product::class.java)
                        if (product != null) {
                            cartProduct.add(product)
                            totalCost += product.price
                            totalCost= (ceil(totalCost*100))/100
                            totalCostView.text = "Total Cost = " + totalCost.toString()
                            notifyItemInserted(cartProduct.size - 1)
                        }
                    }

                })
            }
        }
        mDatabaseReference.addChildEventListener(childEventListener)
        Log.d("anurag", "${cartProduct.size}")
        this.context = context
        Log.d("anurag", "I'm populating ended")

    }

    public class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.cart_product_row_title)
        var image: ImageView = itemView.findViewById(R.id.cart_product_row_photo)
        var price: TextView = itemView.findViewById(R.id.cart_product_row_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DealViewHolder {
        var context = parent.context
        var itemView =
            LayoutInflater.from(context).inflate(R.layout.cart_product_row, parent, false)
        return DealViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return return cartProduct.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        d("anurag", "process${cartProduct.size} ${position}")

        var product = cartProduct[position]
        holder.tvTitle.text = product.title
        holder.price.text = product.price.toString()
        Picasso.get().load(product.photoUrl).into(holder.image)
    }
}