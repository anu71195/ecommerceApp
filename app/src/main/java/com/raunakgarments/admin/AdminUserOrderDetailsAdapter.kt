package com.raunakgarments.admin

import android.content.Intent
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
import com.raunakgarments.global.AdminOrderSingletonClass
import com.raunakgarments.global.OrderStatusObject
import com.raunakgarments.model.UserOrderProduct
import com.squareup.picasso.Picasso

class AdminUserOrderDetailsAdapter : RecyclerView.Adapter<AdminUserOrderDetailsAdapter.AdminUserOrderDetailsViewHolder>() {

    private lateinit var activityIntent: Intent
    var productList: MutableList<UserOrderProduct> = ArrayList()
    var idPositionMap: HashMap<String, Int> = HashMap<String, Int>()
    private lateinit var adminUserOrderDetailsActivity: AdminUserOrderDetailsActivity

    fun populate(intent: Intent, userOrderDetailsActivity: AdminUserOrderDetailsActivity) {
        this.activityIntent = intent
        this.adminUserOrderDetailsActivity = userOrderDetailsActivity

        for (orderedProduct in AdminOrderSingletonClass.userOrders.orders) {
            productList.add(orderedProduct.value)
            userOrderDetailsActivity.populateOrderDeliveryStatusSingletonList(AdminOrderSingletonClass.userOrders.orders)
        }
        Log.d(
            "UserOrderDetailsAdapter",
            "populate-${Gson().toJson(AdminOrderSingletonClass.userOrders)}"
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

        idPositionMap[productList[position].id] = position

        holder.productTitleTv.text = productList[position].title
        Picasso.get().load(productList[position].photoUrl).into(holder.productImageIv)
        holder.productImageIv.layoutParams.width = getScreenWidth() / 3
        holder.totalPriceTv.text =
            "₹" + productList[position].price.toString() + " X " + productList[position].quantity + " = ₹" + productList[position].totalPrice
        holder.deliveryStatusTv.text = "Delivery Status = " + OrderStatusObject.getDeliveryStringFromString(productList[position].deliveryStatus)
        holder.orderStatusTv.text = "Order Status = " + OrderStatusObject.getOrderStringFromString(productList[position].orderStatus)

        holder.deliveryStatusTv.setTextColor(
            OrderStatusObject.getDeliveryColorFromString(productList[position].deliveryStatus))
        holder.orderStatusTv.setTextColor(
            OrderStatusObject.getOrderColorFromString(productList[position].orderStatus))

        orderOrderStatusButtonClickListener(holder,position)
        orderDeliveryStatusButtonClickListener(holder,position)

    }

    private fun orderOrderStatusButtonClickListener(
        holder: AdminUserOrderDetailsViewHolder,
        position: Int
    ) {
        holder.orderStatusTv.setOnClickListener {
            var calculatedPosition = idPositionMap[productList[position].id]
            if(holder.orderStatusTv.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)}") {
                Log.d(
                    "AdminUserOrderDetailsActivity",
                    "orderOrderStatusButtonClickListener :- order status button clicked payment done"
                )
                holder.orderStatusTv.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.refunded)}"
                holder.orderStatusTv.setTextColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.refunded))
                AdminOrderSingletonClass.orderStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.orderStatusList[calculatedPosition].first,OrderStatusObject.orderStatus.refunded)
            } else if (holder.orderStatusTv.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.refunded)}") {
                holder.orderStatusTv.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentPending)}"
                holder.orderStatusTv.setTextColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.paymentPending))
                AdminOrderSingletonClass.orderStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.orderStatusList[calculatedPosition].first,OrderStatusObject.orderStatus.paymentPending)
            } else if(holder.orderStatusTv.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentPending)}") {
                holder.orderStatusTv.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)}"
                holder.orderStatusTv.setTextColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.paymentDone))
                AdminOrderSingletonClass.orderStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.orderStatusList[calculatedPosition].first,OrderStatusObject.orderStatus.paymentDone)
            } else if(holder.orderStatusTv.text.toString() == "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.error)}") {
                holder.orderStatusTv.text = "Order Status = ${OrderStatusObject.getOrderString(OrderStatusObject.orderStatus.paymentDone)}"
                holder.orderStatusTv.setTextColor(OrderStatusObject.getOrderColor(OrderStatusObject.orderStatus.paymentDone))
                AdminOrderSingletonClass.orderStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.orderStatusList[calculatedPosition].first,OrderStatusObject.orderStatus.paymentDone)
            }
        }
    }

    private fun orderDeliveryStatusButtonClickListener(
        holder: AdminUserOrderDetailsViewHolder,
        position: Int
    ) {
        holder.deliveryStatusTv.setOnClickListener {
            var calculatedPosition = idPositionMap[productList[position].id]
            if(holder.deliveryStatusTv.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)}") {
                Log.d(
                    "AdminUserOrderDetailsActivity",
                    "orderOrderStatusButtonClickListener :- order status button clicked payment done"
                )
                holder.deliveryStatusTv.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.delivered)}"
                holder.deliveryStatusTv.setTextColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.delivered))
                AdminOrderSingletonClass.deliveryStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.deliveryStatusList[calculatedPosition].first,OrderStatusObject.deliveryStatus.delivered)
            } else if (holder.deliveryStatusTv.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.delivered)}") {
                holder.deliveryStatusTv.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.cancelled)}"
                holder.deliveryStatusTv.setTextColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.cancelled))
                AdminOrderSingletonClass.deliveryStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.deliveryStatusList[calculatedPosition].first,OrderStatusObject.deliveryStatus.cancelled)
            } else if(holder.deliveryStatusTv.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.cancelled)}") {
                holder.deliveryStatusTv.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.returned)}"
                holder.deliveryStatusTv.setTextColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.returned))
                AdminOrderSingletonClass.deliveryStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.deliveryStatusList[calculatedPosition].first,OrderStatusObject.deliveryStatus.returned)
            } else if(holder.deliveryStatusTv.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.returned)}") {
                holder.deliveryStatusTv.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)}"
                holder.deliveryStatusTv.setTextColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.paymentDone))
                AdminOrderSingletonClass.deliveryStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.deliveryStatusList[calculatedPosition].first,OrderStatusObject.deliveryStatus.paymentDone)
            } else if(holder.deliveryStatusTv.text.toString() == "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.error)}") {
                holder.deliveryStatusTv.text = "Delivery Status = ${OrderStatusObject.getDeliveryString(OrderStatusObject.deliveryStatus.paymentDone)}"
                holder.deliveryStatusTv.setTextColor(OrderStatusObject.getDeliveryColor(OrderStatusObject.deliveryStatus.paymentDone))
                AdminOrderSingletonClass.deliveryStatusList[calculatedPosition!!] = Pair(AdminOrderSingletonClass.deliveryStatusList[calculatedPosition].first,OrderStatusObject.deliveryStatus.paymentDone)
            }
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