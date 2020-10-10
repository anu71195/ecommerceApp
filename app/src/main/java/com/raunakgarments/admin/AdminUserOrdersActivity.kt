package com.raunakgarments.admin

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.UserOrdersAdapter
import com.raunakgarments.model.UserOrderProfile
import kotlinx.android.synthetic.main.activity_admin_user_orders_content_scrolling.*
import kotlinx.android.synthetic.main.activity_user_orders_content_scrolling.*
import kotlinx.android.synthetic.main.activity_user_orders_content_scrolling.activity_user_orders_content_scrolling_OrdersRecyclerView

class AdminUserOrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_orders)
        setSupportActionBar(findViewById(R.id.activity_admin_user_orders_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        initializeAdminUserOrdersRecyclerViewAdapter()
    }

    private fun initializeAdminUserOrdersRecyclerViewAdapter() {
        val adminUserOrdersAdapter = AdminUserOrdersAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
//        productsLayoutManager.reverseLayout = true
//        adminUserOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_user_orders_content_scrolling_OrdersRecyclerView.adapter = adminUserOrdersAdapter
        activity_admin_user_orders_content_scrolling_OrdersRecyclerView.layoutManager = productsLayoutManager
    }
}