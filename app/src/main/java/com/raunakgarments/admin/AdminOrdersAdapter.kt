package com.raunakgarments.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raunakgarments.R

class AdminOrdersAdapter : RecyclerView.Adapter<AdminOrdersAdapter.AdminOrderViewHolder>()  {
    class AdminOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var titleButton: TextView =
            itemView.findViewById(R.id.activity_admin_orders_adapter_admin_orders_row_TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_orders_adapter_admin_orders_row, parent, false)
        return AdminOrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.titleButton.text = "Hello World ${position}"
    }
}