package com.raunakgarments

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.raunakgarments.global.AdminOrderSingletonClass
import com.raunakgarments.global.OrderStatusObject
import com.raunakgarments.helper.FirebaseUtil
import com.raunakgarments.model.ConfirmationCartProduct
import com.raunakgarments.model.UserOrderProduct
import com.raunakgarments.model.UserOrders
import com.squareup.picasso.Picasso
import org.jetbrains.anko.textColor

class UserOrderDetailsAdapter :
    RecyclerView.Adapter<UserOrderDetailsAdapter.UserOrderDetailsViewHolder>() {

    private lateinit var activityIntent: Intent
    private lateinit var userOrders: UserOrders
    var productList: MutableList<UserOrderProduct> = ArrayList()
    private lateinit var userOrderDetailsActivity: UserOrderDetailsActivity

    fun populate(intent: Intent, userOrderDetailsActivity: UserOrderDetailsActivity) {
        this.activityIntent = intent
        this.userOrderDetailsActivity = userOrderDetailsActivity

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

    class UserOrderDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productTitleTv: TextView =
            itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productTitleTextView)
        var productImageIv: ImageView =
            itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productImage)
        var totalPriceTv: TextView =
            itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productTotalPriceTextView)
        var deliveryStatusTv: TextView =
            itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productDeliveryStatusTextView)
        var orderStatusTv: TextView =
            itemView.findViewById(R.id.activity_user_order_details_adapter_user_orders_row_productOrderStatusTextView)
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
        var userOrderFirebaseUtil = FirebaseUtil()
        userOrderFirebaseUtil.openFbReference("userOrders" + "/" + FirebaseAuth.getInstance().uid)
        if (productList[position].deliveryStatus == "") {
            productList[position].deliveryStatus = OrderStatusObject.getOrderStringFromString(userOrders.deliveryStatus)
            userOrderFirebaseUtil.mDatabaseReference.child(userOrders.id).child("orders").child(productList[position].id).child("deliveryStatus").setValue(OrderStatusObject.getOrderStringFromString(userOrders.deliveryStatus))
        }

        if (productList[position].orderStatus == "") {
            productList[position].orderStatus = OrderStatusObject.getDeliveryStringFromString(userOrders.orderStatus)
            userOrderFirebaseUtil.mDatabaseReference.child(userOrders.id).child("orders").child(productList[position].id).child("orderStatus").setValue(OrderStatusObject.getDeliveryStringFromString(userOrders.orderStatus))
        }

        holder.productTitleTv.text = productList[position].title
        Picasso.get().load(productList[position].photoUrl).into(holder.productImageIv)
        holder.productImageIv.layoutParams.width = getScreenWidth() / 3
        holder.totalPriceTv.text =
            "₹" + productList[position].price.toString() + " X " + productList[position].quantity + " = ₹" + productList[position].totalPrice
        holder.deliveryStatusTv.text = "Delivery Status = " + OrderStatusObject.getDeliveryStringFromString(productList[position].deliveryStatus)
        holder.orderStatusTv.text = "Order Status = " + OrderStatusObject.getOrderStringFromString(productList[position].orderStatus)


        holder.deliveryStatusTv.setTextColor(OrderStatusObject.getDeliveryColorFromString(productList[position].deliveryStatus))
        holder.orderStatusTv.setTextColor(OrderStatusObject.getOrderColorFromString(productList[position].orderStatus))

    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        userOrderDetailsActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}