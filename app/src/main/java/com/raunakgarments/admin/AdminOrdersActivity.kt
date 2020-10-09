package com.raunakgarments.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.raunakgarments.R
import kotlinx.android.synthetic.main.activity_admin_orders_content_scrolling.*

class AdminOrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_orders)
        setSupportActionBar(findViewById(R.id.activity_admin_orders_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        initializeUserOrdersRecyclerViewAdapter()
    }

    private fun initializeUserOrdersRecyclerViewAdapter() {
        val adminOrdersAdapter = AdminOrdersAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
        adminOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
        activity_admin_orders_content_scrolling_OrdersRecyclerView.layoutManager = productsLayoutManager
    }
}