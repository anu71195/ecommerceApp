package com.raunakgarments.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raunakgarments.R
import com.raunakgarments.UserOrdersAdapter

class AdminUserOrdersAdapter :
    RecyclerView.Adapter<AdminUserOrdersAdapter.AdminUserOrderViewHolder>() {

    class AdminUserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var title: TextView = itemView.findViewById(R.id.activity_user_orders_adapter_admin_user_orders_row_textViewInformation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUserOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_user_orders_adapter_user_orders_row, parent, false)
        return AdminUserOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminUserOrderViewHolder, position: Int) {
        holder.title.text = "Hello World ${position}"
    }

    override fun getItemCount(): Int {
        return 100
    }
}