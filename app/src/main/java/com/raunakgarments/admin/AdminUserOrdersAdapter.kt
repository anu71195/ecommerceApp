package com.raunakgarments.admin

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.UserOrderProfile
import com.raunakgarments.model.UserOrders

class AdminUserOrdersAdapter :
    RecyclerView.Adapter<AdminUserOrdersAdapter.AdminUserOrderViewHolder>() {

    var userOrdersList: MutableList<UserOrders> = ArrayList()
    private lateinit var adminUserOrdersActivity: Activity
    private lateinit var userOrderProfile: UserOrderProfile

    fun populate(
        userOrdersRef: String,
        intent: Intent,
        adminUserOrdersActivity: AdminUserOrdersActivity
    ) {
        this.adminUserOrdersActivity = adminUserOrdersActivity
        this.userOrderProfile =
            Gson().fromJson<UserOrderProfile>(intent.getStringExtra("userOrderProfile"), UserOrderProfile::class.java)

        d("AdminUserOrdersAdapter", "populate-${Gson().toJson(userOrderProfile)}")


    }

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