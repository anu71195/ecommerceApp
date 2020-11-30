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
import com.raunakgarments.global.AdminOrderSingletonClass
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
        val items = AdminOrderSingletonClass.getItemList()
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
                activity_admin_orders_content_scrolling_StatusFilterOptions.visibility = View.GONE
                when(spinner.selectedItem.toString()) {
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.title) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.customer) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        initializeUserOrdersRecyclerViewByCustomerAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.time) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        initializeUserOrdersrecyclerViewByDatesAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.statusFilter) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        initializeUserOrdersrecyclerViewStatusFilterAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.clean) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                        cleanAdapter()
                    }
                    AdminOrderSingletonClass.getOrderEnumerationTypeString(AdminOrderSingletonClass.OrderEnumerationType.error) -> {
                        d("AdminOrdersActivity", "AdminOrderSingletonClass -> ${spinner.selectedItem.toString()} is selected")
                    }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun cleanAdapter() {
        val adminOrdersAdapter = AdminOrdersAdapter()
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
    }

    private fun initializeUserOrdersrecyclerViewStatusFilterAdapter() {
        activity_admin_orders_content_scrolling_StatusFilterOptions.visibility = View.VISIBLE
        initializeUserOrdersrecyclerViewByDatesAdapter()
    }

    private fun initializeUserOrdersrecyclerViewByDatesAdapter() {
        val adminOrdersAdapter = AdminOrdersAdapterByDates()
        val productsLayoutManager = GridLayoutManager(this, 1)
        productsLayoutManager.reverseLayout = true
        adminOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
        activity_admin_orders_content_scrolling_OrdersRecyclerView.layoutManager =
            productsLayoutManager
    }

    private fun initializeUserOrdersRecyclerViewByCustomerAdapter() {
        val adminOrdersAdapter = AdminOrdersAdapter()
        val productsLayoutManager = GridLayoutManager(this, 1)
        adminOrdersAdapter.populate(getString(R.string.database_userOrders), this)
        activity_admin_orders_content_scrolling_OrdersRecyclerView.adapter = adminOrdersAdapter
        activity_admin_orders_content_scrolling_OrdersRecyclerView.layoutManager =
            productsLayoutManager
    }
}