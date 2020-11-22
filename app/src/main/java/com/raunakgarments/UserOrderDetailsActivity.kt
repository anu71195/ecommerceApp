package com.raunakgarments

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
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
        activity_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${this.userOrders.deliveryStatus}"
        activity_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${this.userOrders.orderStatus}"

        if(this.userOrders.deliveryStatus == "Delivered") {
            activity_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(Color.parseColor("#008000"))
        }

        if (this.userOrders.orderStatus == "Payment Done") {
            activity_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(Color.parseColor("#008000"))
        }

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