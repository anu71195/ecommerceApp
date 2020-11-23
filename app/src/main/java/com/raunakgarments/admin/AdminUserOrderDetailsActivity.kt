package com.raunakgarments.admin

import android.graphics.Color
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.model.UserOrders
import kotlinx.android.synthetic.main.activity_admin_user_order_details_content_scrolling.*
import kotlinx.android.synthetic.main.activity_user_order_details_content_scrolling.*

class AdminUserOrderDetailsActivity : AppCompatActivity() {

    private lateinit var userOrders: UserOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_order_details)
        setSupportActionBar(findViewById(R.id.activity_admin_user_order_details_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        this.userOrders =
            Gson().fromJson<UserOrders>(intent.getStringExtra("userOrders"), UserOrders::class.java)
        activity_admin_user_order_details_content_scrolling_OrdersTotalCost.text = "Total Cost = ₹" + this.userOrders.totalCost
        activity_admin_user_order_details_content_scrolling_OrdersTotalItems.text = "Total Items = ${this.userOrders.orders.size}"
        activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.text = "Delivery Status = ${this.userOrders.deliveryStatus}"
        activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.text = "Order Status = ${this.userOrders.orderStatus}"

        if(this.userOrders.deliveryStatus == "Delivered") {
            activity_admin_user_order_details_content_scrolling_OrdersDeliveryStatus.setBackgroundColor(
                Color.parseColor("#008000"))
        }

        if (this.userOrders.orderStatus == "Payment Done") {
            activity_admin_user_order_details_content_scrolling_OrdersOrderStatus.setBackgroundColor(Color.parseColor("#008000"))
        }

        activity_admin_user_order_details_content_scrolling_OrdersUserName.text = "Name = ${this.userOrders.userOrderProfile.userName}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserAddress.text = "Address = ${this.userOrders.userOrderProfile.address}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserEmail.text = "Email = ${this.userOrders.userOrderProfile.email}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserPhoneNumber.text = "Number = +${this.userOrders.userOrderProfile.areaPhoneCode + " " + this.userOrders.userOrderProfile.number}\n"
        activity_admin_user_order_details_content_scrolling_OrdersUserPincode.text = "Pincode = ${this.userOrders.userOrderProfile.pinCode}"


        d("AdminUserOrderDetailsActivity", "onCreate-${Gson().toJson(userOrders)}")
        initializeAdminUserOrdersRecyclerViewAdapter()
    }

    private fun initializeAdminUserOrdersRecyclerViewAdapter() {
        val adminUserOrderDetailsAdapter =
            AdminUserOrderDetailsAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
//        productsLayoutManager.reverseLayout = true
        adminUserOrderDetailsAdapter.populate(intent, this)
        activity_admin_user_order_details_content_scrolling_OrdersRecyclerView.adapter = adminUserOrderDetailsAdapter
        activity_admin_user_order_details_content_scrolling_OrdersRecyclerView.layoutManager = productsLayoutManager
    }
}