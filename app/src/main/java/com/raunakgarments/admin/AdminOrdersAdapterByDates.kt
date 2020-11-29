package com.raunakgarments.admin

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raunakgarments.R
import com.raunakgarments.model.UserOrderProfile

//todo
class AdminOrdersAdapterByDates : RecyclerView.Adapter<AdminOrdersAdapterByDates.AdminOrderViewHolder>() {
    var userOrderProfileList: MutableList<UserOrderProfile> = ArrayList()
    private lateinit var adminOrdersActivity: Activity

    fun populate(userOrdersRef: String, adminOrdersActivity: AdminOrdersActivity) {

    }

    class AdminOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var titleButton: Button =
            itemView.findViewById(R.id.activity_admin_orders_adapter_by_dates_admin_orders_row_Button)
        var informationTextView: TextView =
            itemView.findViewById(R.id.activity_admin_orders_adapter_by_dates_admin_orders_row_textViewInformation)
        var showDetailsOnInformationTextView = false
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_admin_orders_adapter_by_dates_admin_orders_row, parent, false)
        return AdminOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.titleButton.text = "${position}"
    }

    override fun getItemCount(): Int {
        return 10
    }
}