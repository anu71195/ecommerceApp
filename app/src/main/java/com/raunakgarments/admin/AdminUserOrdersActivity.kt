package com.raunakgarments.admin

import android.os.Bundle
import android.util.Log.d
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.raunakgarments.R
import com.raunakgarments.model.UserOrderProfile
import com.raunakgarments.model.UserOrders

class AdminUserOrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_order)
        setSupportActionBar(findViewById(R.id.activity_admin_user_order_toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24)
        }
        initializeAdminUserOrdersRecyclerViewAdapter()
    }

    private fun initializeAdminUserOrdersRecyclerViewAdapter() {
        var adminUserOrders =
            Gson().fromJson<UserOrderProfile>(intent.getStringExtra("userOrderProfile"), UserOrderProfile::class.java)
        d("AdminUserOrdersActivity", "initializeAdminUserOrdersRecyclerViewAdapter - ${adminUserOrders}")
        d("AdminUserOrdersActivity", "initializeAdminUserOrdersRecyclerViewAdapter - ${Gson().toJson(adminUserOrders)}")
    }
}