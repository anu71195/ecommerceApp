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
import com.raunakgarments.model.CartProduct
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso

class UserCartAdapter : RecyclerView.Adapter<UserCartAdapter.DealViewHolder>() {

    var cartProduct: MutableList<CartProduct> = ArrayList()
    private lateinit var mFirebaseDatebase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var childEventListener: ChildEventListener
    private lateinit var listener: (Product) -> Unit
    private lateinit var context: Context

    fun populate(ref: String, context: Context) {
        var firebaseUtil: FirebaseUtil = FirebaseUtil()
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
                d("tdValu", td.toString())
                var tdProduct = CartProduct()
                tdProduct.populate(snapshot.key.toString(), td.toString())

//                if (td != null) {
//                    td.id = snapshot.key.toString()
//                    cartProduct.add(td)
//                    notifyItemInserted(cartProduct.size - 1)
//                }
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
        var product = cartProduct[position]
        holder.tvTitle.text = "slkdf"
        holder.price.text = "23"
        Picasso.get().load("lksdjf").into(holder.image)
//        holder.tvTitle.setText(product.title)
//        holder.price.text = "\u20b9" + product.price
//        Picasso.get().load(product.photoUrl).into(holder.image)
    }
}