package com.raunakgarments

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.raunakgarments.helper.CostFormatterHelper
import com.raunakgarments.model.CartProduct
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.Profile
import com.squareup.picasso.Picasso

class CartConfirmationAdapter : RecyclerView.Adapter<CartConfirmationAdapter.DealViewHolder>() {

    lateinit var profile: Profile
    lateinit var lockedProducts: HashMap<String, Int>
    lateinit var totalCostView: TextView
    var totalCartCost = 0.0
    var confirmationCartProductArray: MutableList<ConfirmationCartProduct> = ArrayList()

    fun populate(
        ref: String,
        profile: Profile,
        lockedProducts: HashMap<String, Int>,
        totalCostView: TextView
    ) {
        this.profile = profile
        this.lockedProducts = lockedProducts
        this.totalCostView = totalCostView

        var firebaseUtilCart: FirebaseUtil = FirebaseUtil()
        firebaseUtilCart.openFbReference(ref)

        firebaseUtilCart.mDatabaseReference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var confirmationCartProductsMap = snapshot.value as HashMap<String, Int>
                    var firebaseUtilProduct = FirebaseUtil()

                    for (confirmationCartProduct in confirmationCartProductsMap) {
                        firebaseUtilProduct.openFbReference("products/" + confirmationCartProduct.key)
                        firebaseUtilProduct.mDatabaseReference.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                d("cartconfirmationaddapter", snapshot.key.toString())
                                d("cartconfirmationaddapter", snapshot.value.toString())
                                var product = snapshot.getValue(ConfirmationCartProduct::class.java)
                                if (product != null) {
                                    product.quantity =
                                        confirmationCartProductsMap[snapshot.key].toString()
                                            .toDouble()
                                    product.totalPrice =
                                        CostFormatterHelper().formatCost(product.price * product.quantity)
                                    product.productStatus = lockedProducts[snapshot.key]!!
                                    d("cartconfirmationaddapter", "${product.toString()}")
                                    confirmationCartProductArray.add(product)
                                    if (product.productStatus == 1) {
                                        totalCartCost += product.totalPrice
                                    }
                                    totalCartCost =
                                        CostFormatterHelper().formatCost(totalCartCost)//(ceil(totalCost * 100)) / 100
                                }
                                totalCostView.text = "Total Cost = ₹" + totalCartCost.toString()
                                d(
                                    "cartconfirmationaddapter",
                                    "${Gson().toJson(product).toString()}"
                                )
                                notifyItemInserted(confirmationCartProductArray.size - 1)
                            }

                            override fun onCancelled(error: DatabaseError) {}

                        })
                    }
                } else {
                    d("User error", "Cart does not exist for user")
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.cart_confirmation_product_row_title)
        var image: ImageView = itemView.findViewById(R.id.cart_confirmation_product_row_photo)
        var quantity: TextView = itemView.findViewById(R.id.cart_confirmation_product_row_quantity)
        var price: TextView = itemView.findViewById(R.id.cart_confirmation_product_row_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartConfirmationAdapter.DealViewHolder {
        var itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_confirmation_product_row, parent, false)
        return CartConfirmationAdapter.DealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartConfirmationAdapter.DealViewHolder, position: Int) {
        Picasso.get()
            .load(confirmationCartProductArray[position].photoUrl)
            .into(holder.image)
        if (confirmationCartProductArray[position].productStatus == 1) {
            holder.title.text = confirmationCartProductArray[position].title
            holder.quantity.text = confirmationCartProductArray[position].quantity.toString()
            holder.price.text =
                "₹" + confirmationCartProductArray[position].price.toString() + " X " + confirmationCartProductArray[position].quantity.toString() + " = ₹" + confirmationCartProductArray[position].totalPrice.toString()
        } else {
            holder.title.text = "This Product is not available"
        }
    }

    override fun getItemCount(): Int {
        return confirmationCartProductArray.size
    }
}