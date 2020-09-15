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
import com.raunakgarments.model.CartProduct
import com.raunakgarments.model.Profile
import com.squareup.picasso.Picasso

class CartConfirmationAdapter : RecyclerView.Adapter<CartConfirmationAdapter.DealViewHolder>() {

    lateinit var profile: Profile
    lateinit var lockedProducts: HashMap<String, Int>
    lateinit var totalCostView: TextView
    var cartProduct: MutableList<CartProduct> = ArrayList()

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

        firebaseUtilCart.mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var cartProductsMap = snapshot.value as HashMap<String, Int>
                    var firebaseUtilProduct = FirebaseUtil()

                    for(cartProduct in cartProductsMap) {
                        firebaseUtilProduct.openFbReference("products/" + cartProduct.key)
                        firebaseUtilProduct.mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                d("cartconfirmationaddapter", snapshot.key.toString())
                                d("cartconfirmationaddapter", snapshot.value.toString())
                                var product = snapshot.getValue(CartProduct::class.java)
                                if (product != null) {
                                    product.quantity = cartProductsMap[snapshot.key].toString().toDouble()
                                }
                                d("cartconfirmationaddapter", "${Gson().toJson(product).toString()}")
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
        holder.title.text = "Colorful Tshirt"
        Picasso.get()
            .load("https://images-na.ssl-images-amazon.com/images/I/61Ca1VD6fyL._UL1024_.jpg")
            .into(holder.image)
        holder.quantity.text = "3"
        holder.price.text = "₹" + "320" + " X " + "3" + " = ₹" + "960"
    }

    override fun getItemCount(): Int {
        return 3
    }
}