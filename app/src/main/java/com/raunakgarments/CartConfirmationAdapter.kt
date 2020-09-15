package com.raunakgarments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raunakgarments.model.Profile
import com.squareup.picasso.Picasso

class CartConfirmationAdapter : RecyclerView.Adapter<CartConfirmationAdapter.DealViewHolder>() {

    fun populate(profile: Profile, lockedProducts: HashMap<String, Int>, totalCostView: TextView) {

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