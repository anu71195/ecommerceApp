package com.raunakgarments.admin

import android.content.Intent
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.UserOrderDetailsActivity
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.UserOrders
import com.squareup.picasso.Picasso

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
        var productTitleTv: TextView =
            itemView.findViewById(R.id.activity_admin_user_order_details_adapter_user_orders_row_productTitleTextView)
        var productImageIv: ImageView =
            itemView.findViewById(R.id.activity_admin_user_order_details_adapter_user_orders_row_productImage)
        var totalPriceTv: TextView =
            itemView.findViewById(R.id.activity_admin_user_order_details_adapter_user_orders_row_productTotalPriceTextView)
        var deliveryStatusTv: TextView =
            itemView.findViewById(R.id.activity_admin_user_order_details_adapter_user_orders_row_productDeliveryStatusTextView)
        var orderStatusTv: TextView =
            itemView.findViewById(R.id.activity_admin_user_order_details_adapter_user_orders_row_productOrderStatusTextView)
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
        holder.productTitleTv.text = productList[position].title
        Picasso.get().load(productList[position].photoUrl).into(holder.productImageIv)
        holder.productImageIv.layoutParams.width = getScreenWidth() / 3
        holder.totalPriceTv.text =
            "₹" + productList[position].price.toString() + " X " + productList[position].quantity + " = ₹" + productList[position].totalPrice
        holder.deliveryStatusTv.text = "Delivery Status = " + userOrders.deliveryStatus
        holder.orderStatusTv.text = "Order Status = " + userOrders.orderStatus


        //todo create these for each item in order
        if (userOrders.deliveryStatus == "Delivered") {
            holder.deliveryStatusTv.setTextColor(Color.parseColor("#008000"))
        }
        if (userOrders.orderStatus == "Payment Done") {
            holder.orderStatusTv.setTextColor(Color.parseColor("#008000"))
        }
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        adminUserOrderDetailsActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }


    override fun getItemCount(): Int {
        return productList.size
    }
}