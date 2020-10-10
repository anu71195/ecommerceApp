package com.raunakgarments.admin

import android.os.Bundle
import android.util.Log.d
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.UserOrderDetailsAdapter
import com.raunakgarments.model.UserOrders
import kotlinx.android.synthetic.main.activity_admin_user_order_details_content_scrolling.*
import kotlinx.android.synthetic.main.activity_user_order_details_content_scrolling.*
import kotlinx.android.synthetic.main.activity_user_order_details_content_scrolling.activity_user_order_details_content_scrolling_OrdersTotalCost

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

        d("AdminUserOrderDetailsActivity", "onCreate-${Gson().toJson(userOrders)}")
        initializeAdminUserOrdersRecyclerViewAdapter()
    }

    private fun initializeAdminUserOrdersRecyclerViewAdapter() {
        val adminUserOrderDetailsAdapter =
            AdminUserOrderDetailsAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
//        productsLayoutManager.reverseLayout = true
//        adminUserOrderDetailsAdapter.populate(intent, this)
        activity_admin_user_order_details_content_scrolling_OrdersRecyclerView.adapter = adminUserOrderDetailsAdapter
        activity_admin_user_order_details_content_scrolling_OrdersRecyclerView.layoutManager = productsLayoutManager
    }
}