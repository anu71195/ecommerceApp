package com.raunakgarments

import android.content.Context
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.raunakgarments.helper.CostFormatterHelper
import com.raunakgarments.model.CartProduct
import com.raunakgarments.model.Product
import com.squareup.picasso.Picasso

class UserCartAdapter : RecyclerView.Adapter<UserCartAdapter.DealViewHolder>() {

    var cartProduct: MutableList<CartProduct> = ArrayList()
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
                var productQuantity = snapshot.value.toString().toDouble()
                d("anurag", "Parent")
                firebaseUtilProduct.openFbReference("products/" + snapshot.key.toString())
                mFirebaseDatebaseProduct = firebaseUtilProduct.mFirebaseDatabase
                mDatabaseReferenceProduct = firebaseUtilProduct.mDatabaseReference
                mDatabaseReferenceProduct.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}

                    override fun onDataChange(snapshot: DataSnapshot) {
                        d("anurag", "main")
                        d("anurag", "${snapshot.value}")
                        var product = snapshot.getValue(CartProduct::class.java)
                        if (product != null) {
                            product.quantity = productQuantity
                            product.totalPrice =
                                CostFormatterHelper().formatCost(product.price * productQuantity)
                            cartProduct.add(product)
                            totalCost += product.totalPrice
                            totalCost =
                                CostFormatterHelper().formatCost(totalCost)//(ceil(totalCost * 100)) / 100
                            totalCostView.text = "Total Cost = ₹" + totalCost.toString()
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
        var quantity: TextView = itemView.findViewById(R.id.cart_product_row_quantity)
        var addQuantityButton: Button = itemView.findViewById(R.id.cart_product_row_add_quantity)
        var subtractQuantityButton: Button =
            itemView.findViewById(R.id.cart_product_row_subtract_quantity)
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

    private fun addSubtractQuantityClickListener(
        holder: DealViewHolder,
        productId: String,
        position: Int
    ) {
        var productFirebaseUtil = FirebaseUtil()
        productFirebaseUtil.openFbReference("userCart/" + FirebaseAuth.getInstance().uid.toString())

        holder.addQuantityButton.setOnClickListener {
            d("Quantity", "Add")
            var canProductBeAdded = true
            productFirebaseUtil.mDatabaseReference.child(productId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists() && canProductBeAdded) {
                            var number = snapshot.value.toString().toInt()
                            mDatabaseReference.child(productId).setValue(number + 1)
                            cartProduct[position].quantity = (number + 1).toDouble()
                            cartProduct[position].totalPrice = CostFormatterHelper().formatCost(cartProduct[position].price * cartProduct[position].quantity)
                            notifyDataSetChanged()
                        } else if (!snapshot.exists()) {
                            mDatabaseReference.child(productId).setValue(1)
                        }
                        canProductBeAdded = false
                    }
                    override fun onCancelled(error: DatabaseError) {}

                })
        }

        holder.subtractQuantityButton.setOnClickListener {
            d("Quantity", "Subtract")
            var canProductBeSubtracted = true
            productFirebaseUtil.mDatabaseReference.child(productId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists() && canProductBeSubtracted) {
                            var number = snapshot.value.toString().toInt()
                            mDatabaseReference.child(productId).setValue(number - 1)
                            cartProduct[position].quantity = (number - 1).toDouble()
                            cartProduct[position].totalPrice = CostFormatterHelper().formatCost(cartProduct[position].price * cartProduct[position].quantity)
                            notifyDataSetChanged()
                        } else if (!snapshot.exists()) {
                            mDatabaseReference.child(productId).setValue(1)
                        }
                        canProductBeSubtracted = false
                    }
                    override fun onCancelled(error: DatabaseError) {}

                })
        }
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        d("anurag", "process${cartProduct.size} ${position}")

        var product = cartProduct[position]
        holder.tvTitle.text = product.title
        holder.quantity.text = "Quantity = " + product.quantity.toInt().toString()
        holder.price.text = "₹" + product.price.toString() + " X " + product.quantity.toInt()
            .toString() + " = ₹" + product.totalPrice.toString()
        Picasso.get().load(product.photoUrl).into(holder.image)
        addSubtractQuantityClickListener(holder, product.id, position)
    }
}