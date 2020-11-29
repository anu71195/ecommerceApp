package com.raunakgarments.admin

import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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

        setSpinnerDropDown()

    }

    private fun setSpinnerDropDown() {
        val spinner =
            findViewById<Spinner>(R.id.activity_admin_orders_content_scrolling_OrderListTypeSpinner)
        val items = arrayOf("Choose List Type", "500g", "1kg", "2kg", "Clean")
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                arg2: Int,
                arg3: Long
            ) {
                // Do what you want
                val items = spinner.selectedItem.toString()
                if (items == "500g") {
                    d("AdminOrderActivity", "setSpinnerDropDown :- this is 500g of weight...")
                    initializeUserOrdersRecyclerViewAdapter()
                } else if (items == "1kg") {
                    d("AdminOrderActivity", "setSpinnerDropDown :- this is 1kg of weight...")
                } else if (items == "2kg") {
                    d("AdminOrderActivity", "setSpinnerDropDown :- this is 2kg of weight...")
                } else if (items == "Clean") {
                    cleanAdapter()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun cleanAdapter() {
        val adminOrdersAdapter = AdminOrdersAdapter()
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
    }

    private fun initializeUserOrdersRecyclerViewAdapter() {
        val adminOrdersAdapter = AdminOrdersAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
        adminOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
        activity_admin_orders_content_scrolling_OrdersRecyclerView.layoutManager =
            productsLayoutManager
    }
}