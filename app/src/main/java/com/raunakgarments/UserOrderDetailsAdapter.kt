package com.raunakgarments

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.Product
import com.raunakgarments.model.UserOrders

class UserOrderDetailsAdapter :
    RecyclerView.Adapter<UserOrderDetailsAdapter.UserOrderDetailsViewHolder>() {

    private lateinit var activityIntent: Intent
    private lateinit var userOrders: UserOrders
    var productList: MutableList<ConfirmationCartProduct> = ArrayList()

    fun populate(intent: Intent) {
        this.activityIntent = intent
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

    class UserOrderDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productTitle: TextView = itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productTitleTextView)
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
        holder.productTitle.text = productList[position].title
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}