package com.raunakgarments.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raunakgarments.R

class AdminUserOrderDetailsAdapter : RecyclerView.Adapter<AdminUserOrderDetailsAdapter.AdminUserOrderDetailsViewHolder>() {
    class AdminUserOrderDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.activity_admin_user_order_details_adapter_user_orders_rowTitleTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminUserOrderDetailsViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_user_order_details_adapter_user_orders_row, parent, false)
        return AdminUserOrderDetailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminUserOrderDetailsViewHolder, position: Int) {
        holder.title.text = "Hello World ${position}"
    }

    override fun getItemCount(): Int {
        return 100
    }
}