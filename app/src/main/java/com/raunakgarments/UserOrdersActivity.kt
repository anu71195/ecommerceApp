package com.raunakgarments

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_user_orders_content_scrolling.*

class UserOrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_orders)
        setSupportActionBar(findViewById(R.id.activity_user_orders_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }

        initializeUserOrdersRecyclerViewAdapter()
    }

    private fun initializeUserOrdersRecyclerViewAdapter() {
        val userOrdersAdapter = UserOrdersAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
        productsLayoutManager.reverseLayout = true
        userOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_user_orders_content_scrolling_OrdersRecyclerView.adapter = userOrdersAdapter
        activity_user_orders_content_scrolling_OrdersRecyclerView.layoutManager = productsLayoutManager
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}