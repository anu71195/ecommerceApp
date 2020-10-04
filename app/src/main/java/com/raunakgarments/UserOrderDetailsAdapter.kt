package com.raunakgarments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserOrderDetailsAdapter :
    RecyclerView.Adapter<UserOrderDetailsAdapter.UserOrderDetailsViewHolder>() {

    class UserOrderDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_textView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserOrderDetailsViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_user_order_details_adapter_user_orders_row, parent, false)
        return UserOrderDetailsViewHolder(itemView)
    }


    override fun onBindViewHolder(
        holder: UserOrderDetailsViewHolder,
        position: Int
    ) {
        holder.title.text = "Hello World ${position}"
    }

    override fun getItemCount(): Int {
        return 100
    }
}