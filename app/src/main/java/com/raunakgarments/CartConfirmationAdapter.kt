package com.raunakgarments

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartConfirmationAdapter : RecyclerView.Adapter<CartConfirmationAdapter.DealViewHolder>() {
    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.cart_confirmation_product_row_photo)
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
        Picasso.get().load("https://images-na.ssl-images-amazon.com/images/I/61Ca1VD6fyL._UL1024_.jpg").into(holder.image)
    }

    override fun getItemCount(): Int {
        return 3
    }
}