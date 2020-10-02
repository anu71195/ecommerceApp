package com.raunakgarments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserOrdersAdapter: RecyclerView.Adapter<UserOrdersAdapter.UserOrderViewHolder>() {

    fun populate(userOrdersRef: String, userOrdersActivity: UserOrdersActivity) {

    }

    class UserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.activity_user_orders_adapter_user_orders_row_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_orders_adapter_user_orders_row, parent, false)
        return UserOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserOrderViewHolder, position: Int) {
        holder.title.text = "hello ${position}"
    }

    override fun getItemCount(): Int {
        return 10
    }
}