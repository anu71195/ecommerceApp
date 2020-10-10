package com.raunakgarments.admin

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.UserOrderDetailsActivity
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.UserOrders

class AdminUserOrderDetailsAdapter : RecyclerView.Adapter<AdminUserOrderDetailsAdapter.AdminUserOrderDetailsViewHolder>() {

    private lateinit var activityIntent: Intent
    private lateinit var userOrders: UserOrders
    var productList: MutableList<ConfirmationCartProduct> = ArrayList()
    private lateinit var adminUserOrderDetailsActivity: AdminUserOrderDetailsActivity

    fun populate(intent: Intent, userOrderDetailsActivity: AdminUserOrderDetailsActivity) {
        this.activityIntent = intent
        this.adminUserOrderDetailsActivity = userOrderDetailsActivity

        this.userOrders =
            Gson().fromJson<UserOrders>(intent.getStringExtra("userOrders"), UserOrders::class.java)
        for (orderedProduct in userOrders.orders) {
            productList.add(orderedProduct.value)
        }
        Log.d(
            "UserOrderDetailsAdapter",
            "populate-${Gson().toJson(userOrders)}"
        )
        Log.d(
            "UserOrderDetailsAdapter",
            "populate-${Gson().toJson(productList)}"
        )

    }

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