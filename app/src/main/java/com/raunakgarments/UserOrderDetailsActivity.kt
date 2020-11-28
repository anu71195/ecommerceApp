package com.raunakgarments

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.raunakgarments.global.OrderStatusObject
import com.raunakgarments.model.UserOrders
import kotlinx.android.synthetic.main.activity_user_order_details_content_scrolling.*

class UserOrderDetailsActivity : AppCompatActivity() {

    private lateinit var userOrders: UserOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order_details)
        setSupportActionBar(findViewById(R.id.activity_user_order_details_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        this.userOrders =
            Gson().fromJson<UserOrders>(intent.getStringExtra("userOrders"), UserOrders::class.java)
        activity_user_order_details_content_scrolling_OrdersTotalCost.text = "Total Cost = ₹" + this.userOrders.totalCost
        activity_user_order_details_content_scrolling_OrdersTotalItems.text = "Total Items = ${this.userOrders.orders.size}"
        activity_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${OrderStatusObject.getDeliveryStringFromString(userOrders.deliveryStatus)}"
        activity_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${OrderStatusObject.getOrderStringFromString(userOrders.orderStatus)}"

        //todo like admin flow
        activity_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(OrderStatusObject.getDeliveryColorFromString(userOrders.deliveryStatus))
        activity_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(OrderStatusObject.getOrderColorFromString(userOrders.orderStatus))

        activity_user_order_details_content_scrolling_OrdersUserName.text = "Name = ${this.userOrders.userOrderProfile.userName}\n"
        activity_user_order_details_content_scrolling_OrdersUserAddress.text = "Address = ${this.userOrders.userOrderProfile.address}\n"
        activity_user_order_details_content_scrolling_OrdersUserEmail.text = "Email = ${this.userOrders.userOrderProfile.email}\n"
        activity_user_order_details_content_scrolling_OrdersUserPhoneNumber.text = "Number = +${this.userOrders.userOrderProfile.areaPhoneCode + " " + this.userOrders.userOrderProfile.number}\n"
        activity_user_order_details_content_scrolling_OrdersUserPincode.text = "Pincode = ${this.userOrders.userOrderProfile.pinCode}"

        initializeUserOrdersRecyclerViewAdapter()
    }

    private fun initializeUserOrdersRecyclerViewAdapter() {
        val userOrderDetailsAdapter =
            UserOrderDetailsAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
//        productsLayoutManager.reverseLayout = true
        userOrderDetailsAdapter.populate(intent, this)
        activity_user_order_details_content_scrolling_OrdersRecyclerView.adapter = userOrderDetailsAdapter
        activity_user_order_details_content_scrolling_OrdersRecyclerView.layoutManager = productsLayoutManager
    }

}