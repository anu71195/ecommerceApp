package com.raunakgarments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_user_order_details_content_scrolling.*

class UserOrderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_order_details)
        setSupportActionBar(findViewById(R.id.activity_user_order_details_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        activity_user_order_details_content_scrolling_OrdersTotalCost.text = "Hello World"
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